import {Component, Input} from '@angular/core';
import {VehicleData} from "../../dto/VehicleData";

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
  fillColor = 'rgb(255, 0, 0)';

  changeColor() {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    this.fillColor = `rgb(${r}, ${g}, ${b})`;
  }

  latitudeToX(latitude: number) {
    return 10;
  }

  longitudeToY(longitude: number) {
    return 20;
  }
}
