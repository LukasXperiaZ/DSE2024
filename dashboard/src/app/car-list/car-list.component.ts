import {Component, OnInit} from '@angular/core';
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
export class CarListComponent implements OnInit {

  cars: Car[] | undefined;

  enable = false;

  constructor(private inventoryService: InventoryService) {
  }

  ngOnInit() {
    this.getCars();
  }

  getCars() {
    this.inventoryService.getCars()
      .subscribe(cars => this.cars = cars)
  }
}
