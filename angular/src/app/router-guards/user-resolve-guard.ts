import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {map, take} from "rxjs/internal/operators";
import {Utente} from "../commons/models/utente";
import {AuthenticationService} from "../services/authentication.service";

@Injectable()
export class UserResolveGuard implements Resolve<Utente> {
	constructor(private authenticationService: AuthenticationService) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Utente> {
		return this.authenticationService.getUser()
			.pipe(
				take(1),
				map(
					element => {
						return <Utente>element;
					})
			)
	}
}
