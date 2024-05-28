import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {EventLog} from "../dto/EventLog";
import {catchError, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ControlService {

  url: string = "http://localhost:8002/control";

  constructor(private http: HttpClient) { }

  getEventLogs() {
    return this.http.get<EventLog[]>(this.url + "/eventlog")
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
