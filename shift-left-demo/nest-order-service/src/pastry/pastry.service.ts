import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Pastry } from './pastry.dto';

@Injectable()
export class PastryService {

  private readonly resourceUrl: string;

  constructor(configService: ConfigService) {
    this.resourceUrl = configService.get<string>('pastries.baseurl');
    //console.log("this.resourceUrl: " + this.resourceUrl);
  }

  async getPastry(name: string): Promise<Pastry> {
    //console.log("Fetching " + this.resourceUrl + '/pastries/' + name);
    return fetch(this.resourceUrl + '/pastries/' + name)
      .then(response => {
        if (!response.ok) {
          throw new Error('Error while reading Pastry having name ' + name + ', code: ' + response.status);
        }
        return response.json() as Promise<Pastry>;
      });
  }

  async getPastries(size: string): Promise<Pastry[]> {
    return fetch(this.resourceUrl + '/pastries?size=' + size)
      .then(response => {
        if (!response.ok) {
          throw new Error('Error while reading Pastries having size ' + size + ', code: ' + response.status);
        }
        return response.json() as Promise<Pastry[]>;
      });
  }
}
