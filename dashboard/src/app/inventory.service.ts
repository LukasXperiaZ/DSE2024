import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Car} from "../dto/Car";
import {catchError, throwError} from "rxjs";
import {Config} from "../config";

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  // Class responsible for the communication with the InventoryService.

  url: string;

  constructor(private http: HttpClient) {
    if (Config.localDevelopment) {
      this.url = "http://localhost:8001/inventory";
    } else {
      this.url = "http://" + window.location.host + "/inventory";
      console.log("inventory url:  " + this.url)
    }
  }

  getCars() {
    return this.http.get<Car[]>(this.url + "/cars")
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
