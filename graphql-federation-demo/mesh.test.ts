import * as path from "path";

import { DEFAULT_CLI_PARAMS, graphqlMesh, handleFatalError } from '@graphql-mesh/cli';
import { DefaultLogger } from '@graphql-mesh/utils';

import { MicrocksContainer, StartedMicrocksContainer } from '@microcks/microcks-testcontainers';

describe("MeshTests", () => {
  jest.setTimeout(180_000);

  const contractsDir = path.resolve(__dirname, ".", "contracts");
  const datasetsDir = path.resolve(__dirname, ".", "datasets");

  let container: StartedMicrocksContainer;

  beforeAll(async () => {
    // Starting application.
    console.log('Starting application...');
    graphqlMesh(DEFAULT_CLI_PARAMS).catch(e => handleFatalError(e, new DefaultLogger(DEFAULT_CLI_PARAMS.initialLoggerPrefix)));

    console.log('Starting container...');
    container = await new MicrocksContainer('quay.io/microcks/microcks-uber:nightly-native')
      .withMainArtifacts([
        path.resolve(contractsDir, "apipastries-openapi.yaml"),
        path.resolve(contractsDir, "chiefs-service.proto"),
        path.resolve(contractsDir, "stores-graph.graphql")
      ])
      .withSecondaryArtifacts([
        path.resolve(datasetsDir, "apipastries-postman-collection.json"),
        path.resolve(datasetsDir, "apipastries-examples.yaml"),
        path.resolve(datasetsDir, "chiefs-examples.yaml"),
        path.resolve(datasetsDir, "stores-examples.yaml"),
        path.resolve(datasetsDir, "stores-recording.json")
      ])
      .start();
  });

  it("should start, load artifacts and test mesh", async () => {
    console.log("Test")

    await delay(1000);
  });

  afterAll(async () => {
    // Now stop the app, the containers and the network.
    console.log('Stopping application...');
    console.log('Stopping container...');
    await container.stop();
  });

  function delay(ms: number) {
    return new Promise( resolve => setTimeout(resolve, ms) );
  }
});