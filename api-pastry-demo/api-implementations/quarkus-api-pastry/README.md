# quarkus-api-pastry project

This is a sample implementation of the [`API Pastry 2.0.0` OpenAPI](https://github.com/microcks/microcks/blob/master/samples/APIPastry-openapi.yaml) that is a Microcks sample. It is using Quarkus, the Supersonic Subatomic Java Framework. If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

The server is running on port `8282`. 

## Running the application using a container

```
docker run -i --rm -p 8282:8282 quay.io/microcks/quarkus-api-pastry:latest
```

The server is running on port `8282` and we map it on the local `8282` port. 


## Interacting with the application
You can check the API is working with following CURL requests:

```sh
$ curl localhost:8282/pastry                                                                           
[{"name":"Tartelette Fraise","price":2.0,"size":"S","status":"available"},{"name":"Eclair Cafe","price":2.5,"size":"M","status":"available"},{"name":"Divorces","price":2.8,"size":"M","status":"available"},{"name":"Baba Rhum","price":3.2,"size":"L","status":"available"},{"name":"Millefeuille","price":4.4,"size":"L","status":"available"}]

$ curl localhost:8282/pastry/Millefeuille
{"name":"Millefeuille","price":4.4,"size":"L","status":"available"}

$ curl localhost:8282/pastry/Millefeuille -XPATCH -H 'Content-Type: application/json' -d '{"price":4.3}'
{"name":"Millefeuille","price":4.3,"size":"L","status":"available"}

$ # Check JAXB XML serialization
$ curl localhost:8282/pastry -H 'Accept: text/xml'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><pastries><pastry><name>Tartelette Fraise</name><price>2.0</price><size>S</size><status>available</status></pastry><pastry><name>Eclair Cafe</name><price>2.5</price><size>M</size><status>available</status></pastry><pastry><name>Divorces</name><price>2.8</price><size>M</size><status>available</status></pastry><pastry><name>Baba Rhum</name><price>3.2</price><size>L</size><status>available</status></pastry><pastry><name>Millefeuille</name><price>4.3</price><size>L</size><status>available</status></pastry></pastries>

$ curl localhost:8282/pastry/Millefeuille -H 'Accept: text/xml'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><pastry><name>Millefeuille</name><price>4.3</price><size>L</size><status>available</status></pastry>

$ curl localhost:8282/pastry/Millefeuille -XPATCH -H 'Content-Type: text/xml' -H 'Accept: text/xml' -d '<pastry><price>4.2</price></pastry>'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><pastry><name>Millefeuille</name><price>4.2</price><size>L</size><status>available</status></pastry>
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `quarkus-api-pastry-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/quarkus-api-pastry-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/quarkus-api-pastry-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.