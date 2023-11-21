import { Injectable } from '@nestjs/common';

import { OrderInfoDto } from './dto/order-info.dto';

import { Order } from './entities/order.entity';
import { UnavailablePastryError } from './unavailable-pastry.error'

import { Pastry } from '../pastry/pastry.dto';
import { PastryService } from '../pastry/pastry.service';

@Injectable()
export class OrderService {
  constructor(private readonly pastryService: PastryService) {}

  /**
   * 
   * @param orderInfo 
   * @returns 
   * @throws {UnavailablePastryError}
   */
  async create(orderInfo: OrderInfoDto): Promise<Order> {
    let pastryPromises: Promise<Pastry>[] = [];

    for (var i=0; i<orderInfo.productQuantities.length; i++) {
      let productQuantitiy = orderInfo.productQuantities[i];
      pastryPromises.push(this.pastryService.getPastry(productQuantitiy.productName));
    }

    let pastries: PromiseSettledResult<Pastry>[] = await Promise.allSettled(pastryPromises)
    for (var i=0; i<pastries.length; i++) {
      let pastry = pastries[i];
      if (pastry.status === 'fulfilled') {
        if (pastry.value.status != 'available') {
          throw new UnavailablePastryError("Pastry " + pastry.value.name + " is not available", pastry.value.name);
        }
      }
    }
    
    let result = new Order();
    result.customerId = orderInfo.customerId;
    result.productQuantities = orderInfo.productQuantities;
    result.totalPrice = orderInfo.totalPrice;

    return result;
  }
}
