import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable()
export class BasicHttpInterceptor implements HttpInterceptor {

	constructor() {
	}

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		let headers: HttpHeaders = req.headers;
		headers = headers.set('Content-Type', 'application/json');
		// headers = headers.set('Cache-Control', 'no-cache');
		// headers = headers.set('Pragma', 'no-cache');
		// headers = headers.set('If-Modified-Since', 'Sat, 01 Jan 2000 00:00:00 GMT');
		// const token: string = localStorage.getItem('mmj.auth_token');
		// if (token != null && token != undefined) {
		//     headers = headers.set('Authorization', 'Bearer ' + token);
		// }
		return next.handle(req.clone({headers: headers}));
	}
}