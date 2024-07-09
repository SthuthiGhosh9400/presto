ARROW FLIGHT CONNECTOR

Arrow Flight is an RPC framework for high-performance data services based on Arrow data, and is built on top of gRPC .This connector allows querying multiple datasources that supported by Arrow-flight-server.

Apache Arrow enhances performance and efficiency in data-intensive applications through its columnar memory layout, zero-copy reads, vectorized execution, cross-language interoperability, rich data type support, and optimization for modern hardware. These features collectively reduce overhead, improve data processing speeds, and facilitate seamless data exchange between different systems and languages.

Configuration
To configure the Arrow-flight connector, create a catalog properties file in etc/catalog named, for example, arrowmariadb.properties, to mount the Arrow-flight connector as the arrowmariadb catalog. Create the file with the following contents, replacing the connection properties as appropriate for your setup:

To connect CPD-Arrow-flight server

```
data-source.name=mariadb
data-source.host=conops-mariadb-1.csf2wqyc27ft.us-east-1.rds.amazonaws.com
data-source.database=TM_LH_ENGINE_DB_1
data-source.username=TM_LH_ENGINE_USER
data-source.password=<password>
data-source.port=3306
data-source.ssl=true
```

To connect  Arrow-flight-server on cloud

```
connector.name=ibm-arrow-flight
arrow-flight.server=api.dataplatform.dev.cloud.ibm.com
arrow-flight.server.port=443
arrow-flight.apikey=<API_KEY>
arrow-flight.cloud.token-url=https://iam.test.cloud.ibm.com/identity/token
data-source.name=mariadb
data-source.host=conops-mariadb-1.csf2wqyc27ft.us-east-1.rds.amazonaws.com
data-source.database=TM_LH_ENGINE_DB_1
data-source.username=TM_LH_ENGINE_USER
data-source.password=<password>
data-source.port=3306
data-source.ssl=true
```
