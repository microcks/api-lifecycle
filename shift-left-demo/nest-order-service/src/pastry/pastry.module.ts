import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';

import { PastryService } from './pastry.service';

@Module({
  imports: [ConfigModule.forRoot({
    load: [() => ({
      'pastries.baseurl': 'http://localhost:4000/api'
    })],
  })],
  providers: [PastryService],
  exports: [PastryService]
})
export class PastryModule {}
