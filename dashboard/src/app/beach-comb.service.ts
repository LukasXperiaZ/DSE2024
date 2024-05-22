import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {VehicleData} from "../dto/VehicleData";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class BeachCombService {

  url: string = "http://localhost:8000/beachcomb/vehicles/";

  constructor(private http: HttpClient) { }

  getVehicleData(vin: string) {
    return this.http.get<VehicleData>(this.url + vin)
      .pipe(
        catchError(this.handleError)
      )
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}
