import {AbstractService} from "../commons/abstract-service";
import {Search} from "../commons/models/search";
import {HttpClient} from "@angular/common/http";
import {LOCATIONS_URL} from "../constants/constants";
import {Injectable} from "@angular/core";
import {Location} from "../models/location";

@Injectable()
export class LocationsService extends AbstractService<Location> {

    constructor(protected http: HttpClient) {
        super(LOCATIONS_URL, http);
    }

    buildSearch() {
        let search = new Search<Location>(Location);
        search.pageSize = 10;
        this.search = search;
        return search;
    }

    getId(element: Location) {
        return element.uuid;
    }


}