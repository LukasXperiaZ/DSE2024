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
import {Constants} from "../../constants/Constants";
import {BeachCombService} from "../beach-comb.service";

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
              private beachCombService: BeachCombService) {
    this.getCars()
  }

  getCars() {
    this.inventoryService.getCars()
      .subscribe(cars => this.cars = cars)
  }

  getVehicleDataTest() : VehicleData[] {
    let vehicle = new VehicleData("XP7VGCEJXPB204655", new Coordinates(48.198141, Constants.FIRST_LANE_LAT), 110.0, 1, new Date());
    let autonomousVehicle = new VehicleData("5YJ3E7EB7KF240393", new Coordinates(48.199064, Constants.FIRST_LANE_LAT), 100.0, 1, new Date());

    let vehicleSecondLane = new VehicleData("SL", new Coordinates(48.198555, Constants.SECOND_LANE_LAT), 125.0, 2, new Date());
    let vehicleThirdLane = new VehicleData("TL", new Coordinates(48.198777, Constants.THIRD_LANE_LAT), 130.0, 3, new Date());

    let vehicleTopLeft = new VehicleData("TL", new Coordinates(48.199064, Constants.THIRD_LANE_LAT - Constants.LANE_DIFFERENCE/2.0), 130.0, 3, new Date());
    let vehicleBottomRight = new VehicleData("BR", new Coordinates(48.198141, Constants.FIRST_LANE_LAT + Constants.LANE_DIFFERENCE/2.0), 130.0, 3, new Date());

    return [vehicle, autonomousVehicle, vehicleSecondLane, vehicleThirdLane, vehicleTopLeft, vehicleBottomRight]
  }
}
