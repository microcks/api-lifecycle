import * as path from "path";
import { findFreePorts } from "find-free-ports"
import { ConfigModule } from '@nestjs/config';
import { INestApplication } from '@nestjs/common';
import { Test, TestingModule } from '@nestjs/testing';

import { AppModule } from '../src/app.module';

import { TestContainers } from "testcontainers";
import { MicrocksContainer, StartedMicrocksContainer, TestRequest, TestRunnerType } from '@microcks/microcks-testcontainers';

describe('OrderController (e2e)', () => {
    jest.setTimeout(180_000);
    const resourcesDir = path.resolve(__dirname, "..", "test/resources");

    let container: StartedMicrocksContainer;
    let app: INestApplication;
    let appPort: number;
  
    beforeEach(async () => {
      appPort = (await findFreePorts(1, {startPort: 3000, endPort: 3100}))[0];
      await TestContainers.exposeHostPorts(appPort);

      // Start container and load artifacts.
      container = await new MicrocksContainer()
        .withMainArtifacts([
          path.resolve(resourcesDir, 'order-service-openapi.yml'),
          path.resolve(resourcesDir, 'apipastries-openapi.yml')
        ])
        .withSecondaryArtifacts([path.resolve(resourcesDir, 'apipastries-postman-collection.json')])
        .start();

      const moduleFixture: TestingModule = await Test.createTestingModule({
        imports: [
          ConfigModule.forRoot({
            load: [() => ({
              'pastries.baseurl': container.getRestMockEndpoint('API Pastries', '0.0.1')
            })],
          }), AppModule],
      }).compile();
  
      app = moduleFixture.createNestApplication();
      await app.listen(appPort);
    });

    afterEach(async () => {
      // Now stop the container and the network.
      await container.stop();
      await app.close();
    });
  
    it('should conform to OpenAPI spec', async () => {
      var testRequest: TestRequest = {
        serviceId: "Order Service API:0.1.0",
        runnerType: TestRunnerType.OPEN_API_SCHEMA,
        testEndpoint: "http://host.testcontainers.internal:" + appPort,
        timeout: 3000
      };

      /*
      (await container.logs())
        .on("data", line => console.log(line))
        .on("err", line => console.error(line))
        .on("end", () => console.log("Stream closed"));
      */

      var testResult = await container.testEndpoint(testRequest);

      console.log(JSON.stringify(testResult));

      expect(testResult.success).toBe(true);
      expect(testResult.testCaseResults.length).toBe(1);
      expect(testResult.testCaseResults[0].testStepResults.length).toBe(2);
    });
  });