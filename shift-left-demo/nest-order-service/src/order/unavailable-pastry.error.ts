export class UnavailablePastryError extends Error {
  product: string

  constructor(message?: string, product?: string) {
    super(message);
    this.product = product;
  }
}