import { Module } from '@nestjs/common';
import { OrderService } from './order.service';
import { OrderController } from './order.controller';
import { PastryModule } from '../pastry/pastry.module';

@Module({
  imports: [PastryModule],
  controllers: [OrderController],
  providers: [OrderService],
})
export class OrderModule {}
