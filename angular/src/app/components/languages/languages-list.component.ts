import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Language} from "../../models/language";
import {Router} from "@angular/router";
import {LanguagesService} from "../../services/languages.service";
import {ConfirmationService} from "primeng/api";
import {manageErrorCode} from "../../shared/manageErrorCode";

@Component({
    templateUrl: './languages-list.component.html',
})
export class LanguagesListComponent extends AbstractListComponent<Language> implements OnInit {

    constructor(protected router: Router,
                public service: LanguagesService,
                public confirmationService: ConfirmationService) {
        super(router, service);
    }


    elementToEdit: Language;
    elementToDelete: Language;

    ngOnInit(): void {
        if (!this.element) {
            this.element = new Language();
        }
        if (!this.elementToEdit) {
            this.elementToEdit = new Language();
        }
        super.ngOnInit();
    }

    getId() {
        return this.element.uuid;
    }

    toRec(obj: any): Language {
        return obj;
    }


    confirmDelete(elementToDelete: Language) {
        this.elementToDelete = elementToDelete;
        this.confirmationService.confirm({
            message: 'Sei sicuro di voler eliminare questo record?',
            accept: () => {
                this.delete(this.elementToDelete);
            }
        });
    }


    public delete(element: Language) {
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

    editInline(elementToEdit: Language) {
        this.elementToEdit = elementToEdit;
    }

    postDelete() {
        this.elementToDelete = null;
    }


    postSave() {
        super.postSave();
    }
}