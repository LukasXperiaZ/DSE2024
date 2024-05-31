export class Car {
  // Represents a car.
  oem: string;
  model: string;
  vin: string;
  is_self_driving: boolean;

  constructor(oem: string, model: string, vin: string, is_self_driving: boolean) {
    this.oem = oem;
    this.model = model;
    this.vin = vin;
    this.is_self_driving = is_self_driving;
  }
}
