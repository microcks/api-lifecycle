import { ProductQuantity } from "../entities/product-quantity.entity"

export class OrderInfoDto {
  customerId: string
  productQuantities: ProductQuantity[]
  totalPrice: number
}
