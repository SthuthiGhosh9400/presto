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
package com.facebook.plugin.arrow;

import com.facebook.presto.common.type.Type;
import com.facebook.presto.common.type.VarcharType;
import com.facebook.presto.spi.ColumnMetadata;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertEquals;

public class TestArrowColumnHandle
{
    @Test
    public void testConstructorAndGetters()
    {
        String columnName = "TestColumn";
        Type columnType = VarcharType.createUnboundedVarcharType();
        ArrowTypeHandle arrowTypeHandle = new ArrowTypeHandle(1, "VARCHAR", 255, 0, Optional.empty());

        ArrowColumnHandle columnHandle = new ArrowColumnHandle(columnName, columnType, arrowTypeHandle);

        assertEquals(columnHandle.getColumnName(), columnName);
        assertEquals(columnHandle.getColumnType(), columnType);
        assertEquals(columnHandle.getArrowTypeHandle(), arrowTypeHandle);
    }

    @Test
    public void testGetColumnMetadata()
    {
        String columnName = "testcolumn";
        Type columnType = VarcharType.createUnboundedVarcharType();
        ArrowTypeHandle arrowTypeHandle = new ArrowTypeHandle(1, "VARCHAR", 255, 0, Optional.empty());

        ArrowColumnHandle columnHandle = new ArrowColumnHandle(columnName, columnType, arrowTypeHandle);
        ColumnMetadata columnMetadata = columnHandle.getColumnMetadata();

        assertEquals(columnMetadata.getName(), columnName);
        assertEquals(columnMetadata.getType(), columnType);
    }
}
