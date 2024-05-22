import {Component, Input, OnInit} from '@angular/core';
import {VehicleData} from "../../dto/VehicleData";
import {Constants} from "../../constants/Constants";
import {Car} from "../../dto/Car";
import {BeachCombService} from "../beach-comb.service";

@Component({
  selector: 'app-car-svg',
  standalone: true,
  imports: [],
  templateUrl: './car-svg.component.svg',
  styleUrl: './car-svg.component.css'
})
export class CarSvgComponent implements OnInit {
  @Input() cars!: Car[];
  vehicleData: VehicleData[] = [];

  road = 'rgb(211, 211, 211)';
  white = 'rgb(255, 255, 255)';
  car = 'rgb(0, 0, 0)';

  constructor(private beachCombService: BeachCombService) {
  }

  ngOnInit() {
    this.updateVehicleData();
  }

  updateVehicleData() {
    let vehicleData: VehicleData[] = [];
    for (let i = 0; i < this.cars.length; i++) {
      this.beachCombService.getVehicleData(this.cars[i].vin)
        .subscribe(data => {vehicleData.push(data)});
    }
    this.vehicleData = vehicleData;
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
    return 720 - (longitude - minLongitude) / (maxLongitude - minLongitude) * 720;
  }

  getMaxLongitude(): number {
    if (this.vehicleData.length > 0) {
      let maxLongitude = this.vehicleData[0].coordinates.longitude;
      for (let i = 0; i < this.vehicleData.length; i++) {
        let currLongitude = this.vehicleData[i].coordinates.longitude;
        if (currLongitude > maxLongitude) {
          maxLongitude = currLongitude;
        }
      }
      return maxLongitude;
    }
    return -1;
  }

  getMinLongitude(): number {
    if (this.vehicleData.length > 0) {
      let minLongitude = this.vehicleData[0].coordinates.longitude;
      for (let i = 0; i < this.vehicleData.length; i++) {
        let currLongitude = this.vehicleData[i].coordinates.longitude;
        if (currLongitude < minLongitude) {
          minLongitude = currLongitude;
        }
      }
      return minLongitude;
    }
    return -1;
  }
}
