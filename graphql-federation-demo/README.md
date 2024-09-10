## Microcks in GraphQL Federation use-case

This is the demo that was played during the GraphQL SF Meetup #4 on Sept, 5th 2024. You can find the companion [slides here](https://docs.google.com/presentation/d/1_nAPuj7SrCiQF9isEvrtpVQS2VEzjEfwjVQVAXLDk_I/edit?usp=share_link).

### 1st step - Discover Microcks

Start Microcks standalone by going to `/standalone` folder and using the Docker Compose file:

```sh
cd standalone
docker compose up -d
```

After few seconds, Microcks should now run on `http://localhost:8080`. If this port is already used on your machine, edit the `docker-compose.yml` file to change the port number.

#### Import the Pastry OpenAPI resources

Import `contracts/apipastries-openapi.yaml` in Microcks as a primary artifact (default import). Then, import the other files below as secondary artifacts by ticking the **Just merge examples with artifact** checkbox:
* `datasets/apipastries-postman-collection.json`
* `datasets/apipastries-examples.yaml`

Check the **API | Services** menu to play around with this new simulation.

#### Import the Chief Grpc Service resources

Import `contracts/chiefs-service.proto` in Microcks as a primary artifact (default import). Then, import the other files below as secondary artifacts by ticking the **Just merge examples with artifact** checkbox:
* `datasets/chiefs-examples.yaml`

Check the **API | Services** menu to play around with this new simulation. You should also be able to issue those `grpcurl` commands:

```sh
grpcurl -plaintext -d '{}' localhost:9090 io.microcks.chiefs.v1.ChiefsService/ListChiefs

grpcurl -plaintext -d '{"id": 1}' localhost:9090 io.microcks.chiefs.v1.ChiefsService/GetChief

grpcurl -plaintext -d '{"id": 2}' localhost:9090 io.microcks.chiefs.v1.ChiefsService/GetChief

grpcurl -plaintext -d '{"id": 3}' localhost:9090 io.microcks.chiefs.v1.ChiefsService/GetChief
```

#### Import the Stores GraphQL API resources

Import `contracts/stores-graph.Graphql` in Microcks as a primary artifact (default import). Then, import the other files below as secondary artifacts by ticking the **Just merge examples with artifact** checkbox:
* `datasets/stores-examples.yaml`
* `datasets/stores-recording.json`

Check the **API | Services** menu to play around with this new simulation. You should also be able to issue those `curl` commands:

```sh
echo '{ "query":
  "query {
    stores
  }"
}' | tr -d '\n' | curl \
  -X POST \
  -H "Content-Type: application/json" \
  -s -d @- \
  http://localhost:8080/graphql/Store+Graph/1.0


echo '{ "query":
  "query pastrySells($id: ID!) {
    pastrySells(storeId: $id) {
      pastryId
      sellsCount
      monthYear
      storeId
    }
  }",
  "variables": {
    "id": "0"
  }
}' | tr -d '\n' | curl \
  -X POST \
  -H "Content-Type: application/json" \
  -s -d @- \
  http://localhost:8080/graphql/Store+Graph/1.0


echo '{ "query":
  "query pastrySells($id: ID!) {
    pastrySells(storeId: $id) {
      pastryId
      sellsCount
      monthYear
      storeId
    }
  }",
  "variables": {
    "id": "1"
  }
}' | tr -d '\n' | curl \
  -X POST \
  -H "Content-Type: application/json" \
  -s -d @- \
  http://localhost:8080/graphql/Store+Graph/1.0
```

### 2nd step - Automate everything

Start cleaning up the Microcks standalone server you've previously launched. No worries: we'll re-create everything automatically.

```sh
cd standalone
docker compose down
```

Then, create a new instance of Microcks that will be automatically populated will the resources you'll previously loaded manually. From root directory of this project:

```sh
$ ./microcks.sh

==== OUTPUT ===
[+] Running 2/0
 ✔ Container graphql-meetup-microcks-1  Created                              0.0s 
 ✔ Container graphql-meetup-importer-1  Created                              0.0s 
Attaching to graphql-meetup-importer-1, graphql-meetup-microcks-1
graphql-meetup-microcks-1  | 21:22:10,314 |-INFO in ch.qos.logback.classic.LoggerContext[default] - This is logback-classic version 1.4.14
[...]
graphql-meetup-microcks-1  | 21:22:11.083  INFO 1 --- [080-exec-8] i.g.microcks.service.ServiceService      : Having imported 1 services definitions into repository
graphql-meetup-importer-1  | Microcks has discovered 'Store Graph:1.0'
graphql-meetup-importer-1 exited with code 0
```

A new instance of Microcks is now available on `http://localhost:8585`. If this port is already used on your machine, edit the `microcks.yaml` file to change the port number.

Explore this new instance and check the loaded content! Leave your Microcks instance up and runninf in this terminal.

### 3rd step - Play with federation!

In another terminal, we're going to launch a GraphQL Mesh Gateway for doing federation of the 3 simulations powered by Microcks.

You may want to review this mesh configuration if you changed any default port, just edit the `.meshrc.yaml` file and adapt the `endpoints` address.

Open a new terminal window and from the root of this demo project, launch the `install` and then `start` commands:

```sh
npm install
```

and then:

```sh
$ npm run start

==== OUTPUT ====
> start
> mesh dev

[2024-09-10T16:20:38.433Z] INFO  🕸️  Mesh - Server Starting GraphQL Mesh...
[2024-09-10T16:20:38.433Z] INFO  🕸️  Mesh - Server Serving GraphQL Mesh: http://0.0.0.0:4000
[2024-09-10T16:20:38.443Z] INFO  🕸️  Mesh - Pastries Generating GraphQL schema from OpenAPI schema
[2024-09-10T16:20:38.506Z] INFO  🕸️  Mesh - Pastries Processing annotations for the execution layer
[2024-09-10T16:20:38.553Z] INFO  🕸️  Mesh Generating index file in TypeScript
[2024-09-10T16:20:38.603Z] INFO  🕸️  Mesh Writing index.ts for ESM to the disk.
[2024-09-10T16:20:38.604Z] INFO  🕸️  Mesh Writing index.ts for CJS to the disk.

```

A new browser tab should have been opened autoamtically, pointing on `http://localhost:4000/graphql`. On this GraphiQL interface, you may issue some requests and see the effects of federation but using powerful simulations instead of real endpoints.

You can issue this GraphQL query for example:

```graphql
query MyQuery {
  stores {
    id
    location
    name
    pastrySells {
      sellsCount
      monthYear
      pastryId
      pastry {
        name
        price
        size
        description
        chief {
          name
          region
        }
      }
    }
  }
}
```

When you want to finish, just hit `Ctrl+C` in opened terminals and remove existing container resources using `docker compose -f microcks.yaml down`.

That's it!