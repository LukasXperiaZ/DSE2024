import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Car} from "../dto/Car";

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  url: string = "http://localhost:8001/";

  constructor(private http: HttpClient) { }

  getCars() {
    return this.http.get<Car[]>(this.url + "/test")
  }
}
