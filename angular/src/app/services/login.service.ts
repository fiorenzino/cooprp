import {Injectable} from '@angular/core';
import {EMPTY, Observable, throwError} from "rxjs/index";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, map, switchMap} from "rxjs/internal/operators";
import {AppConstants} from "../constants/app-constants";
import {TOKEN_ITEM} from "../constants/constants";
import {AuthenticationService} from "./authentication.service";

@Injectable()
export class LoginService {

	constructor(private httpClient: HttpClient,
				private authenticationService: AuthenticationService) {
	}

	public login(username: string, password: string): Observable<void> {
		return this.httpClient.post<string>(AppConstants.loginUrl, {username: username, password: password}).pipe(
			map(token => {
				localStorage.setItem(TOKEN_ITEM, token);
			}),
			switchMap(() => {
				return this.authenticationService.getUser().pipe(
					map(() => {
						return EMPTY;
					})
				)
			}),
			catchError(this.handleError)
		)
	}

	public logout() {
		localStorage.removeItem(TOKEN_ITEM);
		this.authenticationService.clearUser();
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
