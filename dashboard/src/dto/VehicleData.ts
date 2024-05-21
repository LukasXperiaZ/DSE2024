import {Coordinates} from "./Coordinates";

export class VehicleData {
  vin: string;
  coordinates: Coordinates;
  speed: number;
  lane: number;
  timestamp: Date;

  constructor(vin: string, coordinates: Coordinates, speed: number, lane: number, timestamp: Date) {
    this.vin = vin;
    this.coordinates = coordinates;
    this.speed = speed;
    this.lane = lane;
    this.timestamp = timestamp;
  }
}
