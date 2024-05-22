import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {VehicleData} from "../../dto/VehicleData";
import {Constants} from "../../constants/Constants";
import {NgIf} from "@angular/common";
import {Coordinates} from "../../dto/Coordinates";
import {Car} from "../../dto/Car";
import {BeachCombService} from "../beach-comb.service";

@Component({
  selector: 'app-car-svg',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './car-svg.component.svg',
  styleUrl: './car-svg.component.css'
})
export class CarSvgComponent implements OnInit, OnDestroy {
  @Input() carList!: Car[];
  vehicleData: VehicleData[] = [];

  intervalId: number = -1;

  road = 'rgb(211, 211, 211)';
  white = 'rgb(255, 255, 255)';
  car = 'rgb(0, 0, 0)';
  dist = 'rgb(15,46,169)';

  constructor(private beachCombService: BeachCombService) {
  }

  ngOnInit() {
    this.getVehicleData();

    //setInterval(this.getVehicleData, 5000)
    setInterval(this.getVehicleData.bind(this), 100);
  }

  ngOnDestroy() {
    clearInterval(this.intervalId)
  }

  latitudeToX(latitude: number) {
    let leftBoarder = Constants.THIRD_LANE_LAT - Constants.LANE_DIFFERENCE / 2.0;
    let rightBoarder = Constants.FIRST_LANE_LAT + Constants.LANE_DIFFERENCE / 2.0;
    return (latitude - leftBoarder) / (rightBoarder - leftBoarder) * 640;
  }

  longitudeToY(longitude: number) {
    let maxLongitude = this.getMaxLongitude();
    let minLongitude = this.getMinLongitude();
    // "720 -" since the y coordinate decreases as one goes up
    if (!isNaN(720 - (longitude - minLongitude) / (maxLongitude - minLongitude) * 720)) {
      return 720 - (longitude - minLongitude) / (maxLongitude - minLongitude) * 720;
    }
    return 0;
  }

  getMaxLongitude(): number {
    let maxLongitude = -1;
    for (let i = 0; i < this.vehicleData.length; i++) {
      let currLongitude = this.vehicleData[i].coordinates.longitude;
      if (currLongitude > maxLongitude) {
        maxLongitude = currLongitude;
      }
    }
    return maxLongitude;
  }

  getMinLongitude(): number {
    let minLongitude = 1000000;
    for (let i = 0; i < this.vehicleData.length; i++) {
      let currLongitude = this.vehicleData[i].coordinates.longitude;
      if (currLongitude < minLongitude) {
        minLongitude = currLongitude;
      }
    }
    return minLongitude;
  }

  getVehicleData() {
    let newVehicleData: VehicleData[] = [];
    for (let i = 0; i < this.carList.length; i++) {
      this.beachCombService.getVehicleData(this.carList[i].vin)
        .subscribe(newVehicle => {
            newVehicleData.push(newVehicle);
        }
        );
    }
    this.vehicleData = newVehicleData;
  }

  getDistanceLineCoordinates() {
    let vehicle1 = this.vehicleData[0];
    let vehicle2 = this.vehicleData[1];
    if (vehicle1.vin == "XP7VGCEJXPB204655") {
      let tempVehicle = vehicle2;
      vehicle2 = vehicle1;
      vehicle1 = tempVehicle;
    }

    let vehicle1X = this.latitudeToX(vehicle1.coordinates.latitude)
    let vehicle1Y = this.longitudeToY(vehicle1.coordinates.longitude)

    let vehicle2X = this.latitudeToX(vehicle2.coordinates.latitude)
    let vehicle2Y = this.longitudeToY(vehicle2.coordinates.longitude)

    let distance = this.getDistanceFromLatLonInM(vehicle1.coordinates.latitude, vehicle1.coordinates.longitude,
      vehicle2.coordinates.latitude, vehicle2.coordinates.longitude);

    return [vehicle1X, vehicle1Y, vehicle2X, vehicle2Y, distance]
  }

  // ----- From https://stackoverflow.com/questions/18883601/function-to-calculate-distance-between-two-coordinates -----
  getDistanceFromLatLonInM(lat1: number, lon1: number, lat2: number, lon2: number) {
    var R = 6371000; // Radius of the earth in m
    var dLat = this.deg2rad(lat2-lat1);  // deg2rad below
    var dLon = this.deg2rad(lon2-lon1);
    var a =
      Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) *
      Math.sin(dLon/2) * Math.sin(dLon/2)
    ;
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c; // Distance in m
    return d;
  }

  deg2rad(deg: number) {
    return deg * (Math.PI/180)
  }
  // ----- end -----

  getVehicleDataTest() {
    let vehicle = new VehicleData("XP7VGCEJXPB204655", new Coordinates(48.198141, Constants.FIRST_LANE_LAT));
    let autonomousVehicle = new VehicleData("5YJ3E7EB7KF240393", new Coordinates(48.199064, Constants.FIRST_LANE_LAT));

    let vehicleSecondLane = new VehicleData("SL", new Coordinates(48.198555, Constants.SECOND_LANE_LAT));
    let vehicleThirdLane = new VehicleData("TL", new Coordinates(48.198777, Constants.THIRD_LANE_LAT));

    let vehicleTopLeft = new VehicleData("TL", new Coordinates(48.199064, Constants.THIRD_LANE_LAT - Constants.LANE_DIFFERENCE / 2.0));
    let vehicleBottomRight = new VehicleData("BR", new Coordinates(48.198141, Constants.FIRST_LANE_LAT + Constants.LANE_DIFFERENCE / 2.0));

    this.vehicleData = [vehicle, autonomousVehicle, vehicleSecondLane, vehicleThirdLane, vehicleTopLeft, vehicleBottomRight];
  }

  protected readonly Math = Math;
}
