import {Coordinates} from "./Coordinates";

export class VehicleData {
  vin: string;
  coordinates: Coordinates;
  speed: number;

  constructor(vin: string, coordinates: Coordinates, speed: number) {
    this.vin = vin;
    this.coordinates = coordinates;
    this.speed = speed;
  }
}
