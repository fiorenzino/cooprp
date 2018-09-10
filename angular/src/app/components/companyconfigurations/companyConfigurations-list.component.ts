import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Router} from "@angular/router";
import {CompanyConfigurationsService} from "../../services/companyConfigurations.service";
import {CompanyConfiguration} from "../../models/companyConfiguration";

@Component({
    templateUrl: './companyConfigurations-list.component.html',
})
export class CompanyConfigurationsListComponent extends AbstractListComponent<CompanyConfiguration> implements OnInit {

    constructor(protected router: Router, public service: CompanyConfigurationsService) {
        super (router, service);
    }


    ngOnInit(): void {

        super.ngOnInit();
    }

    getId() {
       return "uuid";
    }

    toRec(obj: any): CompanyConfiguration {
        return obj;
    }

    public view(element: CompanyConfiguration) {
        this.element = element;
        this.router.navigate(['/companyConfigurations/view', this.getId()]);
    }

    public edit(element: CompanyConfiguration) {
        this.element = element;
        this.router.navigate(['/companyConfigurations/edit', this.getId()]);
    }

}