import { Routes } from '@angular/router';
import {CarListComponent} from "./car-list/car-list.component";
import {EventlogComponent} from "./eventlog/eventlog.component";
import {AppComponent} from "./app.component";

export const routes: Routes = [
  {path: '', component: CarListComponent},
  {path: 'carList', component: CarListComponent},
  {path: 'eventLog', component: EventlogComponent}
];
