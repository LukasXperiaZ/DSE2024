import {Component, OnDestroy, OnInit} from '@angular/core';
import {InventoryService} from "../inventory.service";
import {Car} from "../../dto/Car";
import {MatList, MatListItem, MatListItemLine, MatListOption, MatSelectionList} from "@angular/material/list";
import {NgForOf, NgIf} from "@angular/common";
import {MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatDivider} from "@angular/material/divider";
import {MatGridList, MatGridTile, MatGridTileText} from "@angular/material/grid-list";
import {CarSvgComponent} from "../car-svg/car-svg.component";
import {NavigationComponent} from "../navigation/navigation.component";
import {ControlService} from "../control.service";
import {FmStatus} from "../../dto/FmStatus";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatTable,
  MatTableDataSource
} from "@angular/material/table";

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
    MatGridTileText,
    NavigationComponent,
    MatTable,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCellDef,
    MatCell,
    MatColumnDef
  ],
  templateUrl: './car-list.component.html',
  styleUrl: './car-list.component.css'
})
export class CarListComponent implements OnInit, OnDestroy {

  title = 'Live View';

  cars: Car[] | undefined;

  status: FmStatus[] = [];
  intervalId: number = -2;

  constructor(private inventoryService: InventoryService, private controlService: ControlService) {
  }

  ngOnInit() {
    this.getCars();

    setInterval(this.getFollowMeStatus.bind(this), 1000);
  }

  ngOnDestroy() {
    clearInterval(this.intervalId)
  }

  getCars() {
    this.inventoryService.getCars()
      .subscribe(cars => this.cars = cars)
  }

  getFollowMeStatus() {
    this.controlService.getFollowMeStatus()
      .subscribe(fmStatus => {
        this.status = fmStatus;
        console.log(fmStatus)
      })
  }
}
