import {Coordinates} from "./Coordinates";

export class VehicleData {
  vin: string;
  coordinates: Coordinates;

  constructor(vin: string, coordinates: Coordinates) {
    this.vin = vin;
    this.coordinates = coordinates;
  }
}
