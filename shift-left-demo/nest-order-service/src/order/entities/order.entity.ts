import { uid } from 'uid';

import { OrderStatus } from "./order-status"
import { ProductQuantity } from "./product-quantity.entity"

export class Order {
  id: string = uid()
  status: OrderStatus = OrderStatus.CREATED
  customerId: string
  productQuantities: ProductQuantity[]
  totalPrice: number
}
