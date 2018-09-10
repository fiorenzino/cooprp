import {Component, OnInit} from "@angular/core";
import {Language} from "../../modules/language";
import {AbstractEditComponent} from "../../commons/abstract-edit-component";
import {ActivatedRoute, Router} from "@angular/router";
import {LanguagesService} from "../../services/languages.service";

@Component({
    templateUrl: './languages-edit.component.html',
})
export class LanguagesEditComponent extends AbstractEditComponent<Language> implements OnInit {

    constructor(
        protected router: Router,
        protected route: ActivatedRoute,
        public service: LanguagesService
    ) {
        super(router, route, service);
    }

    createInstance(): Language {
        return undefined;
    }

    getId() {
        return "uuid";
    }

    navigateAfterDelete() {
        this.router.navigate(['/languages/list']);
    }

    navigateAfterSave() {
        this.router.navigate(['/languages/list']);

    }

    navigateAfterUpdate() {
        this.router.navigate(['/languages/list']);

    }

    navigateToList() {
        this.router.navigate(['/languages/list']);

    }

    toRec(obj: any): Language {
        return undefined;
    }

}