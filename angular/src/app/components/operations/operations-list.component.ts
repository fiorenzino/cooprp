import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Language} from "../../models/language";
import {Router} from "@angular/router";
import {LanguagesService} from "../../services/languages.service";

@Component({
    templateUrl: './languages-list.component.html',
})
export class OperationsListComponent extends AbstractListComponent<Language> implements OnInit {

    constructor(protected router: Router, public service: LanguagesService) {
        super (router, service);
    }


    ngOnInit(): void {

        super.ngOnInit();
    }

    getId() {
       return "uuid";
    }

    toRec(obj: any): Language {
        return obj;
    }

    public view(element: Language) {
        this.element = element;
        this.router.navigate(['/languages/view', this.getId()]);
    }

    public edit(element: Language) {
        this.element = element;
        this.router.navigate(['/languages/edit', this.getId()]);
    }

}