import {Injectable} from '@angular/core';
import {Observable, of, throwError} from "rxjs";
import {Utente} from "../commons/models/utente";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {AppConstants} from "../constants/app-constants";
import {catchError, map} from "rxjs/internal/operators";

@Injectable()
export class AuthenticationService {

    private url: string = AppConstants.userUrl;
    private utente: Utente;

    constructor(private httpClient: HttpClient) {
    }

    public getUser(): Observable<Utente> {
        if (this.utente == null) {
            return this.httpClient.get<Utente>(this.url).pipe(
                map(utente => {
                    this.utente = utente;
                    return this.utente;
                }),
                catchError(this.handleError)
            )
        } else {
            return of(this.utente);
        }
    }

    public clearUser() {
        this.utente = null;
    }

    private handleError(error: HttpErrorResponse): Observable<any> {
        if (error == null) {
            return throwError('Server error');
        }
        console.error(error);
        if (error.status === 403) {
            return throwError('Forbidden');
        }
        return throwError(error['error'] || 'Server error');
    }
}
