import { Component } from '@angular/core';
import {InventoryService} from "../inventory.service";
import {Car} from "../../dto/Car";
import {MatList, MatListItem, MatListItemLine, MatListOption, MatSelectionList} from "@angular/material/list";
import {NgForOf, NgIf} from "@angular/common";
import {MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatDivider} from "@angular/material/divider";
import {MatGridList, MatGridTile, MatGridTileText} from "@angular/material/grid-list";
import {VehicleData} from "../../dto/VehicleData";
import {Coordinates} from "../../dto/Coordinates";
import {CarSvgComponent} from "../car-svg/car-svg.component";

@Component({
  selector: 'app-car-list',
  standalone: true,
  imports: [
    MatList,
    MatListItem,
    MatSelectionList,
    MatListOption,
    NgIf,
    MatIconButton,
    MatIcon,
    NgForOf,
    MatDivider,
    MatGridList,
    MatGridTile,
    MatListItemLine,
    CarSvgComponent,
    MatGridTileText
  ],
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.css'
})
export class CarListComponent {

  cars: Car[] | undefined

  vehicleData: VehicleData[] | undefined

  constructor(private inventoryService: InventoryService,
              ) {
    this.getCars()
  }

  getCars() {
    this.inventoryService.getCars()
      .subscribe(cars => this.cars = cars)
  }

  getVehicleData() : VehicleData[] {
    let vehicle = new VehicleData("XP7VGCEJXPB204655", new Coordinates(48.198141, 16.370004), 110.0, 1, new Date());
    let autonomousVehicle = new VehicleData("5YJ3E7EB7KF240393", new Coordinates(48.199064, 16.370004), 100.0, 1, new Date());

    let vehicleTest = new VehicleData("TEST1", new Coordinates(48.198555, 16.369975), 125.0, 2, new Date());
    let vehicleTest1 = new VehicleData("TEST2", new Coordinates(48.198777, 16.369946), 130.0, 3, new Date());

    return [vehicle, autonomousVehicle, vehicleTest, vehicleTest1]
  }
}
