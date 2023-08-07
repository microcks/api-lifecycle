# Shift-left demo - Spring Boot Order Service

This application is a sample on how to integrate Microcks via Testcontainers within your development inner-loop.

## Application introduction

TODO

## Development phase

```shell
$ mvn spring-boot:run
==== OUTPUT ====
[...]
[INFO] Attaching agents: []

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.2)

2023-08-07T16:53:45.004+02:00  INFO 60867 --- [           main] org.acme.order.OrderServiceApplication   : Starting OrderServiceApplication using Java 17.0.6 with PID 60867 (/Users/laurent/Development/github/api-lifecycle/shift-left-demo/spring-boot-order-service/target/classes started by laurent in /Users/laurent/Development/github/api-lifecycle/shift-left-demo/spring-boot-order-service)
2023-08-07T16:53:45.006+02:00  INFO 60867 --- [           main] org.acme.order.OrderServiceApplication   : No active profile set, falling back to 1 default profile: "default"
2023-08-07T16:53:45.291+02:00  INFO 60867 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2023-08-07T16:53:45.295+02:00  INFO 60867 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2023-08-07T16:53:45.295+02:00  INFO 60867 --- [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.11]
2023-08-07T16:53:45.326+02:00  INFO 60867 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2023-08-07T16:53:45.326+02:00  INFO 60867 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 304 ms
2023-08-07T16:53:45.443+02:00  INFO 60867 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2023-08-07T16:53:45.447+02:00  INFO 60867 --- [           main] org.acme.order.OrderServiceApplication   : Started OrderServiceApplication in 0.556 seconds (process running for 0.683)
```

```shell
$ ./microcks.sh
==== OUTPUT ====
[+] Running 3/2
 ✔ Network spring-boot-order-service_default       Created                                                                                                                                                                         0.0s 
 ✔ Container spring-boot-order-service-microcks-1  Created                                                                                                                                                                         0.1s 
 ✔ Container spring-boot-order-service-importer-1  Created                                                                                                                                                                         0.0s 
Attaching to spring-boot-order-service-importer-1, spring-boot-order-service-microcks-1
spring-boot-order-service-microcks-1  | exec java -javaagent:/opt/agent-bond/agent-bond.jar=jolokia{{host=0.0.0.0}},jmx_exporter{{9779:/opt/agent-bond/jmx_exporter_config.yml}} -XX:+UseParallelGC -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=40 -XX:+ExitOnOutOfMemoryError -cp . -jar /deployments/app.jar
spring-boot-order-service-importer-1  | Got error when invoking Microcks client retrieving config: Get "http://microcks:8080/api/keycloak/config": dial tcp 172.22.0.2:8080: connect: connection refused
spring-boot-order-service-microcks-1  | I> No access restrictor found, access to any MBean is allowed
spring-boot-order-service-microcks-1  | Jolokia: Agent started with URL http://172.22.0.2:8778/jolokia/
spring-boot-order-service-importer-1 exited with code 1
spring-boot-order-service-importer-1  | Got error when invoking Microcks client retrieving config: Get "http://microcks:8080/api/keycloak/config": dial tcp 172.22.0.2:8080: connect: connection refused
spring-boot-order-service-importer-1 exited with code 1
spring-boot-order-service-microcks-1  | 
spring-boot-order-service-microcks-1  |   .   ____          _            __ _ _
spring-boot-order-service-microcks-1  |  /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
spring-boot-order-service-microcks-1  | ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
spring-boot-order-service-microcks-1  |  \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
spring-boot-order-service-microcks-1  |   '  |____| .__|_| |_|_| |_\__, | / / / /
spring-boot-order-service-microcks-1  |  =========|_|==============|___/=/_/_/_/
spring-boot-order-service-microcks-1  | 
spring-boot-order-service-microcks-1  |  :: Spring Boot ::                (v3.1.1)
spring-boot-order-service-microcks-1  | 
spring-boot-order-service-microcks-1  | 
spring-boot-order-service-microcks-1  | 14:54:19.830  INFO 1 --- [      main] i.g.microcks.MicrocksApplication         : Starting MicrocksApplication using Java 17.0.8 with PID 1 (/deployments/app.jar started by ? in /deployments)
spring-boot-order-service-microcks-1  | 14:54:19.831 DEBUG 1 --- [      main] i.g.microcks.MicrocksApplication         : Running with Spring Boot v3.1.1, Spring v6.0.10
spring-boot-order-service-microcks-1  | 14:54:19.832  INFO 1 --- [      main] i.g.microcks.MicrocksApplication         : The following 1 profile is active: "uber"
spring-boot-order-service-importer-1  | Got error when invoking Microcks client retrieving config: Get "http://microcks:8080/api/keycloak/config": dial tcp 172.22.0.2:8080: connect: connection refused
spring-boot-order-service-importer-1 exited with code 1
spring-boot-order-service-importer-1  | Got error when invoking Microcks client retrieving config: Get "http://microcks:8080/api/keycloak/config": dial tcp 172.22.0.2:8080: connect: connection refused
spring-boot-order-service-importer-1 exited with code 1
spring-boot-order-service-microcks-1  | 14:54:20.858  INFO 1 --- [      main] i.g.microcks.config.WebConfiguration     : Starting web application configuration, using profiles: [uber]
spring-boot-order-service-microcks-1  | 14:54:20.859  INFO 1 --- [      main] i.g.microcks.config.WebConfiguration     : Web application fully configured
spring-boot-order-service-microcks-1  | 14:54:20.894  INFO 1 --- [      main] i.g.m.c.EmbeddedMongoConfiguration       : Creating a new embedded Mongo Java Server with in-memory persistence
spring-boot-order-service-microcks-1  | 14:54:20.984  INFO 1 --- [      main] de.bwaldvogel.mongo.MongoServer          : started MongoServer(port: 38853, ssl: false)
spring-boot-order-service-microcks-1  | 14:54:21.044  INFO 1 --- [      main] org.mongodb.driver.client                : MongoClient with metadata {"driver": {"name": "mongo-java-driver|sync", "version": "4.9.1"}, "os": {"type": "Linux", "name": "Linux", "architecture": "aarch64", "version": "5.15.49-linuxkit-pr"}, "platform": "Java/Red Hat, Inc./17.0.8+7-LTS"} created with settings MongoClientSettings{readPreference=primary, writeConcern=WriteConcern{w=null, wTimeout=null ms, journal=null}, retryWrites=true, retryReads=true, readConcern=ReadConcern{level=null}, credential=null, streamFactoryFactory=null, commandListeners=[], codecRegistry=ProvidersCodecRegistry{codecProviders=[ValueCodecProvider{}, BsonValueCodecProvider{}, DBRefCodecProvider{}, DBObjectCodecProvider{}, DocumentCodecProvider{}, CollectionCodecProvider{}, IterableCodecProvider{}, MapCodecProvider{}, GeoJsonCodecProvider{}, GridFSFileCodecProvider{}, Jsr310CodecProvider{}, JsonObjectCodecProvider{}, BsonCodecProvider{}, EnumCodecProvider{}, com.mongodb.client.model.mql.ExpressionCodecProvider@238bfd6c, com.mongodb.Jep395RecordCodecProvider@ef1695a]}, loggerSettings=LoggerSettings{maxDocumentLength=1000}, clusterSettings={hosts=[localhost:38853], srvServiceName=mongodb, mode=SINGLE, requiredClusterType=UNKNOWN, requiredReplicaSetName='null', serverSelector='null', clusterListeners='[]', serverSelectionTimeout='30000 ms', localThreshold='30000 ms'}, socketSettings=SocketSettings{connectTimeoutMS=10000, readTimeoutMS=0, receiveBufferSize=0, sendBufferSize=0}, heartbeatSocketSettings=SocketSettings{connectTimeoutMS=10000, readTimeoutMS=10000, receiveBufferSize=0, sendBufferSize=0}, connectionPoolSettings=ConnectionPoolSettings{maxSize=100, minSize=0, maxWaitTimeMS=120000, maxConnectionLifeTimeMS=0, maxConnectionIdleTimeMS=0, maintenanceInitialDelayMS=0, maintenanceFrequencyMS=60000, connectionPoolListeners=[], maxConnecting=2}, serverSettings=ServerSettings{heartbeatFrequencyMS=10000, minHeartbeatFrequencyMS=500, serverListeners='[]', serverMonitorListeners='[]'}, sslSettings=SslSettings{enabled=false, invalidHostNameAllowed=false, context=null}, applicationName='null', compressorList=[], uuidRepresentation=UNSPECIFIED, serverApi=null, autoEncryptionSettings=null, contextProvider=null}
spring-boot-order-service-microcks-1  | 14:54:21.052  INFO 1 --- [er-worker2] d.b.m.wire.MongoWireProtocolHandler      : client [id: 0xd0edb0db, L:/127.0.0.1:38853 - R:/127.0.0.1:47050] connected
spring-boot-order-service-microcks-1  | 14:54:21.052  INFO 1 --- [er-worker1] d.b.m.wire.MongoWireProtocolHandler      : client [id: 0x6e116023, L:/127.0.0.1:38853 - R:/127.0.0.1:47048] connected
spring-boot-order-service-microcks-1  | 14:54:21.090  INFO 1 --- [host:38853] org.mongodb.driver.cluster               : Monitor thread successfully connected to server with description ServerDescription{address=localhost:38853, type=STANDALONE, state=CONNECTED, ok=true, minWireVersion=0, maxWireVersion=6, maxDocumentSize=16777216, logicalSessionTimeoutMinutes=null, roundTripTimeNanos=47156291}
spring-boot-order-service-microcks-1  | 14:54:21.292  INFO 1 --- [      main] i.g.m.config.AICopilotConfiguration      : AICopilot is disabled
spring-boot-order-service-importer-1  | Got error when invoking Microcks client retrieving config: Get "http://microcks:8080/api/keycloak/config": dial tcp 172.22.0.2:8080: connect: connection refused
spring-boot-order-service-microcks-1  | 14:54:21.709  INFO 1 --- [      main] i.g.microcks.MicrocksApplication         : Started MicrocksApplication in 2.069 seconds (process running for 2.633)
[...]
spring-boot-order-service-importer-1  | Microcks has discovered 'API Pastries:0.0.1'
[...]
spring-boot-order-service-importer-1  | Microcks has discovered 'API Pastries:0.0.1'
[...]
spring-boot-order-service-importer-1 exited with code 0
```

```shell
$ curl -XPOST localhost:8080/api/orders -H 'Content-type: application/json' \
    -d '{"customerId": "lbroudoux", "productQuantities": [{"productName": "Millefeuille", "quantity": 1}], "totalPrice": 10.1}'
==== OUTPUT ====
{"id":"58d8ff56-d484-431e-8c94-6747b7b8674b","status":"CREATED","customerId":"lbroudoux","productQuantities":[{"productName":"Millefeuille","quantity":1}],"totalPrice":10.1}  
```
## Unit Test phase

### Mock your dependencies

```java
@Container
public static MicrocksContainer microcksContainer = new MicrocksContainer("quay.io/microcks/microcks-uber:nightly");
```

```java
@BeforeAll
public static void setup() throws Exception {
  microcksContainer.importAsMainArtifact(new File("target/test-classes/third-parties/apipastries-openapi.yaml"));
  microcksContainer.importAsSecondaryArtifact(new File("target/test-classes/third-parties/apipastries-postman-collection.json"));
}
```

```java
@DynamicPropertySource
static void configureProperties(DynamicPropertyRegistry registry) {
   String url = microcksContainer.getRestMockEndpoint("API Pastries", "0.0.1");
   registry.add("pastries.baseUrl", () -> url);
}
```

```java
@Test
public void testGetPastries(){
   // Test our API client and check that arguments and responses are correctly serialized.
   List<Pastry> pastries=client.listPastries("S");
   assertEquals(1,pastries.size());
}
```

### OpenAPI contract testing

```java
@Container
public static MicrocksContainer microcksContainer = new MicrocksContainer("quay.io/microcks/microcks-uber:nightly")
      .withAccessToHost(true);
```

```java
@BeforeAll
public static void setup() throws Exception {
  // Load the System Under Test OpenAPI contract into Microcks.
  microcksContainer.importAsMainArtifact(new File("target/classes/order-service-openapi.yaml"));
  // Load also the third-party ones so that RestTemplate will also use Microcks mocks 👻
  microcksContainer.importAsMainArtifact(new File("target/test-classes/third-parties/apipastries-openapi.yaml"));
  microcksContainer.importAsSecondaryArtifact(new File("target/test-classes/third-parties/apipastries-postman-collection.json"));
}
```

```java
@LocalServerPort
private Integer port;
```

```java
@BeforeEach
public void setupPort() {
  // Host port exposition should be done here.
  Testcontainers.exposeHostPorts(port);
}

@DynamicPropertySource
static void configureProperties(DynamicPropertyRegistry registry) {
  String url = microcksContainer.getRestMockEndpoint("API Pastries", "0.0.1");
  registry.add("pastries.baseUrl", () -> url);
}
```

```java
@Test
public void testOpenAPIContract() throws Exception {
  // Ask for an Open API conformance to be launched.
  TestRequest testRequest = new TestRequest.Builder()
        .serviceId("Order Service API:0.1.0")
        .runnerType(TestRunnerType.OPEN_API_SCHEMA.name())
        .testEndpoint("http://host.testcontainers.internal:" + port + "/api")
        .build();

  TestResult testResult = microcksContainer.testEndpoint(testRequest);
  assertTrue(testResult.isSuccess());
```


### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/#build-image)

