import { Controller, Post, Body, Res, HttpStatus } from '@nestjs/common';
import { Response } from 'express';

import { OrderService } from './order.service';
import { UnavailablePastryError } from './unavailable-pastry.error';

import { OrderInfoDto } from './dto/order-info.dto';
import { UnavailableProduct } from './dto/unavailable-product.dto';


@Controller('orders')
export class OrderController {
  constructor(private readonly orderService: OrderService) {}

  @Post()
  async create(@Body() orderInfo: OrderInfoDto, @Res() res: Response) {
    let result = null;
    try {
      result = await this.orderService.create(orderInfo);
    } catch (err) {
      if (err instanceof UnavailablePastryError) {
        return res.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .json(new UnavailableProduct(err.product, err.message))
            .send();
      } else {
        return res.status(HttpStatus.INTERNAL_SERVER_ERROR).send();
      }
    }

    return res.status(HttpStatus.CREATED).json(result).send();
  }
}
