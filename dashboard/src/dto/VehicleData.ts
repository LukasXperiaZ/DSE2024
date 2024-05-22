import {Coordinates} from "./Coordinates";

export class VehicleData {
  vin: string;
  coordinates: Coordinates;

  constructor(vin: string, coordinates: Coordinates, speed: number, lane: number, timestamp: Date) {
    this.vin = vin;
    this.coordinates = coordinates;
  }
}
