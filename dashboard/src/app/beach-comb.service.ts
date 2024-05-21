import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class BeachCombService {

  url: string = "http://localhost:800___TODO___";

  constructor(private http: HttpClient) { }

  getVehicleData() {
    // TODO
  }
}
