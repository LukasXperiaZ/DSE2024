import {Component, Input} from '@angular/core';
import {VehicleData} from "../../dto/VehicleData";
import {Constants} from "../../constants/Constants";

@Component({
  selector: 'app-car-svg',
  standalone: true,
  imports: [],
  templateUrl: './car-svg.component.svg',
  styleUrl: './car-svg.component.css'
})
export class CarSvgComponent {
  @Input() vehicleData!: VehicleData[]

  road = 'rgb(211, 211, 211)';
  white = 'rgb(255, 255, 255)';
  car = 'rgb(0, 0, 0)';

  latitudeToX(latitude: number) {
    let leftBoarder = Constants.THIRD_LANE_LAT - Constants.LANE_DIFFERENCE / 2.0;
    let rightBoarder = Constants.FIRST_LANE_LAT + Constants.LANE_DIFFERENCE / 2.0;
    let pixelValue = (latitude - leftBoarder) / (rightBoarder - leftBoarder) * 570;
    console.log("pixelValue: " + pixelValue);
    return pixelValue;
  }

  longitudeToY(longitude: number) {
    let maxLongitude = this.getMaxLongitude();
    let minLongitude = this.getMinLongitude();
    // "720 -" since the y coordinate decreases as one goes up
    let pixelValue = 720 - (longitude - minLongitude) / (maxLongitude - minLongitude) * 720;
    console.log("pixelValue: " + pixelValue);
    return pixelValue;
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
