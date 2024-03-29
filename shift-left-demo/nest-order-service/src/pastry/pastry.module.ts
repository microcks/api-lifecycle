import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';

import { PastryService } from './pastry.service';

@Module({
  imports: [ConfigModule.forRoot({
    load: [() => ({
      'pastries.baseurl': 'http://localhost:9090/rest/API+Pastries/0.0.1'
    })],
  })],
  providers: [PastryService],
  exports: [PastryService]
})
export class PastryModule {}
