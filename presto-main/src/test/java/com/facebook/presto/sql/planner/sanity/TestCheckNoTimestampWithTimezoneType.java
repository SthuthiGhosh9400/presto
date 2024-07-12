/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.sql.planner.sanity;

import com.facebook.presto.Session;
import com.facebook.presto.metadata.Metadata;
import com.facebook.presto.spi.WarningCollector;
import com.facebook.presto.spi.plan.PlanNode;
import com.facebook.presto.spi.plan.PlanNodeIdAllocator;
import com.facebook.presto.spi.relation.VariableReferenceExpression;
import com.facebook.presto.sql.parser.SqlParser;
import com.facebook.presto.sql.planner.TypeProvider;
import com.facebook.presto.sql.planner.assertions.BasePlanTest;
import com.facebook.presto.sql.planner.iterative.rule.test.PlanBuilder;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.function.Function;

import static com.facebook.presto.SessionTestUtils.TEST_SESSION;
import static com.facebook.presto.common.type.BooleanType.BOOLEAN;
import static com.facebook.presto.common.type.TimestampWithTimeZoneType.TIMESTAMP_WITH_TIME_ZONE;
import static com.facebook.presto.common.type.VarcharType.VARCHAR;
import static com.facebook.presto.sql.planner.iterative.rule.test.PlanBuilder.assignment;
import static com.facebook.presto.testing.TestingSession.testSessionBuilder;

public class TestCheckNoTimestampWithTimezoneType
        extends BasePlanTest
{
    private Session testSession;
    private Metadata metadata;
    private SqlParser sqlParser;
    private PlanNodeIdAllocator idAllocator = new PlanNodeIdAllocator();

    @BeforeClass
    public void setup()
    {
        Session.SessionBuilder sessionBuilder = testSessionBuilder()
                .setCatalog("local")
                .setSchema("tiny");
        testSession = sessionBuilder.build();
        metadata = getQueryRunner().getMetadata();
        sqlParser = getQueryRunner().getSqlParser();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown()
    {
        testSession = null;
        metadata = null;
        sqlParser = null;
        idAllocator = null;
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Timestamp with Timezone type is not supported in Prestissimo")
    public void testValidateProjectFail()
    {
        validatePlan(
                p -> {
                    VariableReferenceExpression col = p.variable("col", VARCHAR);
                    VariableReferenceExpression col2 = p.variable("col2", TIMESTAMP_WITH_TIME_ZONE);
                    return p.project(
                            assignment(col2, p.rowExpression("cast(col as timestamp with time zone)")),
                            p.values(col));
                });
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Timestamp with Timezone type is not supported in Prestissimo")
    public void testValidateProjectAssignmentFail()
    {
        validatePlan(
                p -> {
                    VariableReferenceExpression col = p.variable("col", VARCHAR);
                    VariableReferenceExpression col1 = p.variable("col1", VARCHAR);
                    VariableReferenceExpression col2 = p.variable("col2", BOOLEAN);
                    return p.project(
                            assignment(col2, p.rowExpression("cast(col as timestamp with time zone) > cast(col1 as timestamp with time zone)")),
                            p.values(col, col1));
                });
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Timestamp with Timezone type is not supported in Prestissimo")
    public void testValidateValueFail()
    {
        validatePlan(
                p -> {
                    VariableReferenceExpression col = p.variable("col", TIMESTAMP_WITH_TIME_ZONE);
                    VariableReferenceExpression col2 = p.variable("col2", VARCHAR);
                    return p.project(
                            assignment(col2, p.rowExpression("cast(col as varchar)")),
                            p.values(col));
                });
    }

    private void validatePlan(Function<PlanBuilder, PlanNode> planProvider)
    {
        PlanBuilder builder = new PlanBuilder(TEST_SESSION, idAllocator, metadata);
        PlanNode planNode = planProvider.apply(builder);
        TypeProvider types = builder.getTypes();
        getQueryRunner().inTransaction(testSession, session -> {
            session.getCatalog().ifPresent(catalog -> metadata.getCatalogHandle(session, catalog));
            new CheckNoTimestampWithTimezoneType().validate(planNode, session, metadata, sqlParser, types, WarningCollector.NOOP);
            return null;
        });
    }
}
