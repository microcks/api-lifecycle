# Shift-left demo - Quarkus Order Service

This application is a sample on how to use Microcks DevServices for Quarkus within your development inner-loop.

## Application introduction

TODO

## Development phase

```shell
$ ./mvnw compile quarkus:dev
==== OUTPUT ====
[...]
Listening for transport dt_socket at address: 5005
2023-08-09 12:25:26,496 INFO  [io.git.mic.qua.dep.DevServicesMicrocksProcessor] (build-29) The 'default' microcks container is ready on http://localhost:9191
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2023-08-09 12:25:26,990 INFO  [io.quarkus] (Quarkus Main Thread) order-service 0.1.0-SNAPSHOT on JVM (powered by Quarkus 3.2.3.Final) started in 1.126s. Listening on: http://localhost:8080
2023-08-09 12:25:26,990 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2023-08-09 12:25:26,991 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [cdi, microcks, rest-client-reactive, rest-client-reactive-jackson, resteasy-reactive, resteasy-reactive-jackson, smallrye-context-propagation, vertx]
2023-08-09 12:27:02,441 INFO  [io.quarkus] (Shutdown thread) order-service stopped in 0.013s


--
Press [e] to edit command line args (currently ''), [:] for the terminal, [h] for more options>ut, [:] for the terminal, [h] for more options>
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

```shell
$ curl -XPOST localhost:8080/api/orders -H 'Content-type: application/json' \
    -d '{"customerId": "lbroudoux", "productQuantities": [{"productName": "Millefeuille", "quantity": 1}], "totalPrice": 10.1}'
==== OUTPUT ====    
{"id":"83661708-a75e-4fef-a354-331eef9102db","status":"CREATED","customerId":"lbroudoux","productQuantities":[{"productName":"Millefeuille","quantity":1}],"totalPrice":10.1}
``` 

## Unit Test phase

### Mock your dependencies

These are already mocks thanks to the configuration you put into `application.properties`:

```properties
quarkus.microcks.devservices.artifacts.primaries=target/classes/order-service-openapi.yaml,target/test-classes/third-parties/apipastries-openapi.yaml
quarkus.microcks.devservices.artifacts.secondaries=target/test-classes/third-parties/apipastries-postman-collection.json
```

### OpenAPI contract testing

Microcks container launched by DevService is automatically able to reach localhost on `quarkus.http.test-port` using the `host.testcontainers.internal` hostname.

```java
@ConfigProperty(name= "quarkus.http.test-port")
int quarkusHttpPort;
```

```java
@ConfigProperty(name= "quarkus.microcks.default")
String microcksContainerUrl;
```

```java
@Inject
ObjectMapper mapper;
```

```java
@Test
public void testOpenAPIContract() throws Exception {
  // Ask for an Open API conformance to be launched.
  TestRequest testRequest = new TestRequest.Builder()
        .serviceId("Order Service API:0.1.0")
        .runnerType(TestRunnerType.OPEN_API_SCHEMA.name())
        .testEndpoint("http://host.testcontainers.internal:" + quarkusHttpPort + "/api")
        .build();

  TestResult testResult = MicrocksContainer.testEndpoint(microcksContainerUrl, testRequest);

  // You may inspect complete response object with following:
  //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testResult));

  assertTrue(testResult.isSuccess());
}
```

## Related Guides

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
