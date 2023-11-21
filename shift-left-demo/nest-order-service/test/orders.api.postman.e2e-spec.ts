import * as path from "path";
import { findFreePorts } from "find-free-ports"
import { ConfigModule } from '@nestjs/config';
import { INestApplication } from '@nestjs/common';
import { Test, TestingModule } from '@nestjs/testing';

import { AppModule } from '../src/app.module';

import { Network, StartedNetwork, TestContainers } from "testcontainers";
import { MicrocksContainersEnsemble, StartedMicrocksContainersEnsemble, TestRequest, TestRunnerType } from '@microcks/microcks-testcontainers';

describe('OrderController (e2e)', () => {
    jest.setTimeout(180_000);
    const resourcesDir = path.resolve(__dirname, "..", "test/resources");

    let network: StartedNetwork;
    let ensemble: StartedMicrocksContainersEnsemble;
    let app: INestApplication;
    let appPort: number;
  
    beforeAll(async () => {
      appPort = (await findFreePorts(1, {startPort: 3000, endPort: 3100}))[0];
      await TestContainers.exposeHostPorts(appPort);

      // Start ensemble and load artifacts.
      network = await new Network().start();
      ensemble = await new MicrocksContainersEnsemble(network)
        .withMainArtifacts([
          path.resolve(resourcesDir, 'order-service-openapi.yml'),
          path.resolve(resourcesDir, 'apipastries-openapi.yml')
        ])
        .withSecondaryArtifacts([
          path.resolve(resourcesDir, 'order-service-postman-collection.json'),
          path.resolve(resourcesDir, 'apipastries-postman-collection.json')
        ])
        .start();

      const moduleFixture: TestingModule = await Test.createTestingModule({
        imports: [
          ConfigModule.forRoot({
            load: [() => ({
              'pastries.baseurl': ensemble.getMicrocksContainer().getRestMockEndpoint('API Pastries', '0.0.1')
            })],
          }), AppModule],
      }).compile();
  
      app = moduleFixture.createNestApplication();
      await app.listen(appPort);
    });

    afterAll(async () => {
      // Now stop the ensemble and the network.
      await ensemble.stop();
      await network.stop();
      await app.close();
    });

    it ('should conform to Postman rules', async () => {
      var testRequest: TestRequest = {
        serviceId: "Order Service API:0.1.0",
        runnerType: TestRunnerType.POSTMAN,
        testEndpoint: "http://host.testcontainers.internal:" + appPort,
        timeout: 3000
      };

      var testResult = await ensemble.getMicrocksContainer().testEndpoint(testRequest);

      console.log(JSON.stringify(testResult));

      expect(testResult.success).toBe(true);
      expect(testResult.testCaseResults.length).toBe(1);
      expect(testResult.testCaseResults[0].testStepResults.length).toBe(2);
    });
  });