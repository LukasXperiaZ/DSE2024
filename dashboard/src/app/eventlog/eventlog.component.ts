import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {NavigationComponent} from "../navigation/navigation.component";
import {MatPaginator, MatPaginatorModule} from "@angular/material/paginator";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {EventLog, Type} from "../../dto/EventLog";
import {NgIf} from "@angular/common";
import {ControlService} from "../control.service";
import {MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-eventlog',
  standalone: true,
  imports: [
    NavigationComponent,
    MatTableModule,
    MatPaginatorModule,
    NgIf,
    MatIconButton,
    MatIcon,
    MatProgressSpinner
  ],
  templateUrl: './eventlog.component.html',
  styleUrl: './eventlog.component.css'
})
export class EventlogComponent implements AfterViewInit, OnInit {
  // Responsible for visualizing the EventLogs.

  title = 'Event Log'
  displayedColumns: string[] = ['_id', 'type', 'timestamp', 'lv', 'fv', 'message', 'successive_check_fails', 'speed_mismatch', 'lane_mismatch'];
  dataSource: MatTableDataSource<EventLog> = new MatTableDataSource<EventLog>();

  loading: boolean = true;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatPaginator) set matPaginator(mp: MatPaginator) {
    this.paginator = mp;
    this.dataSource.paginator = this.paginator;
  }

  constructor(private controlService: ControlService) {
  }

  ngOnInit() {
    this.getEventLogs();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  getEventLogs() {
    this.loading = true;
    this.controlService.getEventLogs()
      .subscribe(eventLogs => {
        this.dataSource = new MatTableDataSource<EventLog>(eventLogs);
        console.log("Updated event logs!")
        this.loading = false;
      });
  }

  printDate(date: string) {
    return new Date(date).toLocaleString();
  }

}

// Just for testing purposes. Can be used while developing / improving the dashboard.
const ELEMENT_DATA: EventLog[] = [
  {
    _id: {
      $oid: "6654b347563f56cd49886157"
    },
    type: Type.FM_check_fail,
    timestamp: {
      $date: new Date("2024-05-27T16:22:31.318Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655",
    message: "Speed Missmatch",
    successive_check_fails: 1,
    speed_mismatch: 10,
    lane_mismatch: "OK"
  },
  {
    _id: {
      $oid: "6654b338563f56cd49886156"
    },
    type: Type.FM_start,
    timestamp: {
      $date: new Date("2024-05-27T16:22:16.050Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655"
  },
  {
    _id: {
      $oid: "6654b332563f56cd49886154"
    },
    type: Type.FM_end,
    timestamp: {
      $date: new Date("2024-05-27T16:22:10.966Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655"
  },
  {
    _id: {
      $oid: "6654b347563f56cd49886157"
    },
    type: Type.FM_check_fail,
    timestamp: {
      $date: new Date("2024-05-27T16:22:31.318Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655",
    message: "Speed Missmatch",
    successive_check_fails: 1,
    speed_mismatch: 10,
    lane_mismatch: "OK"
  },
  {
    _id: {
      $oid: "6654b338563f56cd49886156"
    },
    type: Type.FM_start,
    timestamp: {
      $date: new Date("2024-05-27T16:22:16.050Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655"
  },
  {
    _id: {
      $oid: "6654b332563f56cd49886154"
    },
    type: Type.FM_end,
    timestamp: {
      $date: new Date("2024-05-27T16:22:10.966Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655"
  },
  {
    _id: {
      $oid: "6654b347563f56cd49886157"
    },
    type: Type.FM_check_fail,
    timestamp: {
      $date: new Date("2024-05-27T16:22:31.318Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655",
    message: "Speed Missmatch",
    successive_check_fails: 1,
    speed_mismatch: 10,
    lane_mismatch: "OK"
  },
  {
    _id: {
      $oid: "6654b338563f56cd49886156"
    },
    type: Type.FM_start,
    timestamp: {
      $date: new Date("2024-05-27T16:22:16.050Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655"
  },
  {
    _id: {
      $oid: "6654b332563f56cd49886154"
    },
    type: Type.FM_end,
    timestamp: {
      $date: new Date("2024-05-27T16:22:10.966Z")
    },
    lv: "5YJ3E7EB7KF240393",
    fv: "XP7VGCEJXPB204655"
  }
];
