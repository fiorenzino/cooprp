import {Component, OnInit} from '@angular/core';
import {TopMenuService} from "../../services/top-menu.service";
import {Utente} from "../../commons/models/utente";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";

@Component({
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    public utente: Utente;
    public combined: any = {};

    constructor(private topMenuService: TopMenuService,
                public router: Router,
                private authenticationService: AuthenticationService,
    ) {
        this.utente = new Utente();
    }

    ngOnInit() {
        this.topMenuService.activateMenu('home');
        this.authenticationService.getUser().subscribe(utente => {
            this.combined['cooprp'] = {attive: {}, passive: {}};
            if (utente == null || utente.ruoli == null || utente.ruoli.length === 0) {
                console.log('should not happen. we have CAS!');
            }

            this.utente = utente;
        });
    }

}
