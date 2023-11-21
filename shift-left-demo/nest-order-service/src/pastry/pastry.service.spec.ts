import * as path from "path";
import { ConfigModule } from '@nestjs/config';
import { Test, TestingModule } from '@nestjs/testing';

import { MicrocksContainer, StartedMicrocksContainer } from "@microcks/microcks-testcontainers";

import { Pastry } from './pastry.dto';
import { PastryService } from './pastry.service';

describe('PastryService', () => {
  jest.setTimeout(180_000);
  const resourcesDir = path.resolve(__dirname, "../..", "test/resources");

  let container: StartedMicrocksContainer;
  let service: PastryService;

  beforeEach(async () => {
    // Start container and load artifacts.
    container = await new MicrocksContainer()
      .withMainArtifacts([path.resolve(resourcesDir, 'apipastries-openapi.yml')])
      .withSecondaryArtifacts([path.resolve(resourcesDir, 'apipastries-postman-collection.json')])
      .start();

    const module: TestingModule = await Test.createTestingModule({
      imports: [ConfigModule.forRoot({
        load: [() => ({
          'pastries.baseurl': container.getRestMockEndpoint('API Pastries', '0.0.1')
        })],
      })],
      providers: [PastryService],
    }).compile();

    service = module.get<PastryService>(PastryService);
  });

  afterEach(async () => {
    // Now stop the container.
    await container.stop();
  });

  it('should retrieve pastry by name', async () => {
    let pastry: Pastry = await service.getPastry('Millefeuille');
    expect(pastry.name).toBe("Millefeuille");
    expect(pastry.status).toBe("available");

    pastry = await service.getPastry('Eclair Cafe');
    expect(pastry.name).toBe("Eclair Cafe");
    expect(pastry.status).toBe("available");

    pastry = await service.getPastry('Eclair Chocolat');
    expect(pastry.name).toBe("Eclair Chocolat");
    expect(pastry.status).toBe("unknown");
  });

  it('should retrieve pastries by size', async () => {
    let pastries: Pastry[] = await service.getPastries('S');
    expect(pastries.length).toBe(1);

    pastries = await service.getPastries('M');
    expect(pastries.length).toBe(2);

    pastries = await service.getPastries('L');
    expect(pastries.length).toBe(2);
  });
});
