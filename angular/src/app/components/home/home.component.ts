import {Component, OnInit} from '@angular/core';
import {TopMenuService} from "../../services/top-menu.service";
import {Utente} from "../../commons/models/utente";
import {AuthenticationService} from "../../services/authentication.service";
import {Router} from "@angular/router";
import {CompanyConfigurationsService} from "../../services/companyConfigurations.service";
import {LanguagesService} from "../../services/languages.service";
import {LocationsService} from "../../services/locations.service";
import {NotificationsService} from "../../services/notifications.service";
import {OperationsService} from "../../services/operations.service";
import {WorkshiftsService} from "../../services/workshifts.service";

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
                private companyConfigurationService: CompanyConfigurationsService,
                private languagesService: LanguagesService,
                private locationsService: LocationsService,
                private notificationsService: NotificationsService,
                private operationsService: OperationsService,
                private workshiftsService: WorkshiftsService,
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
        this.companyConfigurationService.buildSearch();
        this.companyConfigurationService.size().subscribe(
            res => this.combined['companyConfigurations'] = res
        );
        this.languagesService.buildSearch();
        this.languagesService.size().subscribe(
            res => this.combined['languages'] = res
        );
        this.locationsService.buildSearch();
        this.locationsService.size().subscribe(
            res => this.combined['locations'] = res
        );
        this.notificationsService.buildSearch();
        this.notificationsService.size().subscribe(
            res => this.combined['notifications'] = res
        );
        this.operationsService.buildSearch();
        this.operationsService.size().subscribe(
            res => this.combined['operations'] = res
        );
        this.workshiftsService.buildSearch();
        this.workshiftsService.size().subscribe(
            res => this.combined['workshifts'] = res
        );
    }

}
