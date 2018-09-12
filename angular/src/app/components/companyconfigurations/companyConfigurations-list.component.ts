import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Router} from "@angular/router";
import {CompanyConfigurationsService} from "../../services/companyConfigurations.service";
import {CompanyConfiguration} from "../../models/companyConfiguration";
import {Language} from "../../models/language";
import {manageErrorCode} from "../../shared/manageErrorCode";
import {ConfirmationService} from "primeng/api";

@Component({
    templateUrl: './companyConfigurations-list.component.html',
})
export class CompanyConfigurationsListComponent extends AbstractListComponent<CompanyConfiguration> implements OnInit {

    constructor(protected router: Router,
                public service: CompanyConfigurationsService,
                public confirmationService: ConfirmationService) {
        super(router, service);
    }


    elementToEdit: CompanyConfiguration;
    elementToDelete: CompanyConfiguration;

    ngOnInit(): void {
        if (!this.element) {
            this.element = new CompanyConfiguration();
        }
        super.ngOnInit();
    }

    getId() {
        return this.element.uuid;
    }

    toRec(obj: any): CompanyConfiguration {
        return obj;
    }


    confirmDelete(elementToDelete: CompanyConfiguration) {
        this.elementToDelete = elementToDelete;
        this.confirmationService.confirm({
            message: 'Sei sicuro di voler eliminare questo record?',
            accept: () => {
                this.delete(this.elementToDelete);
            }
        });
    }


    public delete(element: CompanyConfiguration) {
        this.clearMsgs();
        this.service.delete(this.elementToDelete.uuid).subscribe(
            result => {
                this.addInfo('Eliminazione completata con successo. ');
                this.elementToDelete = this.newElement();
                this.loaddata(false);
                this.postDelete();
            },
            error => {
                manageErrorCode(
                    'Impossibile completare la eliminazione',
                    this.msgs,
                    this.router
                );
            }
        );
    }


    public update() {
        this.clearMsgs();
        this.service.update(this.elementToEdit).subscribe(
            element => {
                this.addInfo('Modifica completata con successo. ');
                this.elementToEdit = null;
            },
            error => {
                manageErrorCode(
                    'Impossibile completare la modifica',
                    this.msgs,
                    this.router
                );
            }
        );
    }

    public cancelEdit() {
        this.elementToEdit = null;
    }

    editInline(elementToEdit: CompanyConfiguration) {
        this.elementToEdit = elementToEdit;
    }

    postDelete() {
        this.elementToDelete = null;
    }


    postSave() {
        super.postSave();
    }

}