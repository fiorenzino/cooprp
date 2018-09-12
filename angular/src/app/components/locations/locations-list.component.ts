import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Location} from "../../models/location";
import {Router} from "@angular/router";
import {LocationsService} from "../../services/locations.service";
import {manageErrorCode} from "../../shared/manageErrorCode";
import {ConfirmationService} from "primeng/api";

@Component({
    templateUrl: './locations-list.component.html',
})
export class LocationsListComponent extends AbstractListComponent<Location> implements OnInit {

    constructor(protected router: Router, public service: LocationsService, public confirmationService: ConfirmationService) {
        super(router, service);
    }


    elementToEdit: Location;
    elementToDelete: Location;

    ngOnInit(): void {
        if (!this.element) {
            this.element = new Location();
        }
        super.ngOnInit();
    }

    getId() {
        return this.element.uuid;
    }

    toRec(obj: any): Location {
        return obj;
    }

    confirmDelete(elementToDelete: Location) {
        this.elementToDelete = elementToDelete;
        this.confirmationService.confirm({
            message: 'Sei sicuro di voler eliminare questo record?',
            accept: () => {
                this.delete(this.elementToDelete);
            }
        });
    }


    public delete(element: Location) {
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

    editInline(elementToEdit: Location) {
        this.elementToEdit = elementToEdit;
    }

    postDelete() {
        this.elementToDelete = null;
    }


    postSave() {
        super.postSave();
    }
}