import {Component, OnInit} from '@angular/core';
import {TopMenuService} from "../../services/top-menu.service";
import {AuthenticationService} from "../../services/authentication.service";
import {Utente} from "../../commons/models/utente";
import {ConfirmationService} from "primeng/api";
import {LoginService} from "../../services/login.service";
import {Router} from "@angular/router";

@Component({
	selector: 'main-header',
	templateUrl: './main-header.component.html',
	styleUrls: ['./main-header.component.css']
})
export class MainHeaderComponent implements OnInit {

	public active: string;
	public utente: Utente;

	constructor(private topMenuService: TopMenuService,
				private authenticationService: AuthenticationService,
				private confirmationService: ConfirmationService,
				private loginService: LoginService,
				private router: Router) {
	}

	ngOnInit() {
		this.topMenuService.activateMenuEvent.subscribe(
			(menu) => {
				this.active = menu;
			}
		);

		this.authenticationService.getUser().subscribe(
			utente => {
				this.utente = utente;
			}
		)
	}

	public confirm() {
		this.confirmationService.confirm({
			message: 'Vuoi uscire?',
			accept: () => {
				this.loginService.logout();
				this.router.navigate(['/login']);
			}
		});
	}

}
