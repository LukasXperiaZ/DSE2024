export class Car {
  private oem: string
  private model: string
  private vin: string
  private is_self_driving: boolean

  constructor(oem: string, model: string, vin: string, is_self_driving: boolean) {
    this.oem = oem;
    this.model = model;
    this.vin = vin;
    this.is_self_driving = is_self_driving;
  }
}
