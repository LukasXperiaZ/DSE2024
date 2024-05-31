import {Coordinates} from "./Coordinates";

export class VehicleData {
  // Represents the data of a vehicle
  vin: string;
  coordinates: Coordinates;
  speed: number;

  constructor(vin: string, coordinates: Coordinates, speed: number) {
    this.vin = vin;
    this.coordinates = coordinates;
    this.speed = speed;
  }
}
