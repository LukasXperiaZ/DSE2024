import { Component } from '@angular/core';
import {InventoryService} from "../inventory.service";
import {Car} from "../../dto/Car";

@Component({
  selector: 'app-car-list',
  standalone: true,
  imports: [],
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.css'
})
export class CarListComponent {

  cars: Car[] | undefined;

  constructor(private inventoryService: InventoryService) {
  }

  getCars() {
    this.inventoryService.getCars()
      .subscribe(cars => this.cars = cars)
  }
}
