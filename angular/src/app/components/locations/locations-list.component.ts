import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Location} from "../../models/location";
import {Router} from "@angular/router";
import {LocationsService} from "../../services/locations.service";

@Component({
    templateUrl: './locations-list.component.html',
})
export class LocationsListComponent extends AbstractListComponent<Location> implements OnInit {

    constructor(protected router: Router, public service: LocationsService) {
        super (router, service);
    }


    ngOnInit(): void {

        super.ngOnInit();
    }

    getId() {
       return "uuid";
    }

    toRec(obj: any): Location {
        return obj;
    }

    public view(element: Location) {
        this.element = element;
        this.router.navigate(['/locations/view', this.getId()]);
    }

    public edit(element: Location) {
        this.element = element;
        this.router.navigate(['/locations/edit', this.getId()]);
    }

}