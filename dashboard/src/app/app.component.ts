import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {CarListComponent} from "./car-list/car-list.component";
import {NavigationComponent} from "./navigation/navigation.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CarListComponent, NavigationComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
}
