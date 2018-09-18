import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Workshift} from "../../models/workshift";
import {Router} from "@angular/router";
import {WorkshiftsService} from "../../services/workshifts.service";

@Component({
    templateUrl: './workshifts-list.component.html',
})
export class WorkshiftsListComponent extends AbstractListComponent<Workshift> implements OnInit {

    constructor(protected router: Router, public service: WorkshiftsService) {
        super(router, service);
    }


    ngOnInit(): void {

        super.ngOnInit();
    }

    getId() {
        return this.element.uuid;
    }

    toRec(obj: any): Workshift {
        return obj;
    }

    public view(element: Workshift) {
        this.element = element;
        this.router.navigate(['/workshifts/view', this.getId()]);
    }

    public edit(element: Workshift) {
        this.element = element;
        this.router.navigate(['/workshifts/edit', this.getId()]);
    }


    newElement(): Workshift {
        return new Workshift();
    }
}