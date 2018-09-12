import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Operation} from "../../models/operation";
import {Router} from "@angular/router";
import {OperationsService} from "../../services/operations.service";

@Component({
    templateUrl: './operations-list.component.html',
})
export class OperationsListComponent extends AbstractListComponent<Operation> implements OnInit {

    constructor(protected router: Router, public service: OperationsService) {
        super (router, service);
    }


    ngOnInit(): void {

        super.ngOnInit();
    }

    getId() {
       return this.element.uuid;
    }

    toRec(obj: any): Operation {
        return obj;
    }

    public view(element: Operation) {
        this.element = element;
        this.router.navigate(['/operations/view', this.getId()]);
    }

    public edit(element: Operation) {
        this.element = element;
        this.router.navigate(['/operations/edit', this.getId()]);
    }

}