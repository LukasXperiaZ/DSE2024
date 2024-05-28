import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-navigation',
  standalone: true,
  imports: [
    MatButton,
    RouterLink
  ],
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.css'
})
export class NavigationComponent {

}
