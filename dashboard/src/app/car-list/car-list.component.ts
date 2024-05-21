import { Component } from '@angular/core';
import {InventoryService} from "../inventory.service";
import {Car} from "../../dto/Car";
import {MatList, MatListItem, MatListItemLine, MatListOption, MatSelectionList} from "@angular/material/list";
import {NgForOf, NgIf} from "@angular/common";
import {MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatDivider} from "@angular/material/divider";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";

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
    MatListItemLine
  ],
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.css'
})
export class CarListComponent {

  cars: Car[] | undefined

  constructor(private inventoryService: InventoryService,
              ) {
    this.getCars()
  }

  getCars() {
    this.inventoryService.getCars()
      .subscribe(cars => this.cars = cars)
  }
}
