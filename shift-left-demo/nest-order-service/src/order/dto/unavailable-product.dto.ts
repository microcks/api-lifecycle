export class UnavailableProduct {
  productName: string
  details: string

  constructor(productName: string, details: string) {
    this.productName = productName;
    this.details = details;
  }
}