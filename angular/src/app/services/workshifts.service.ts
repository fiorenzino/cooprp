import {AbstractService} from "../commons/abstract-service";
import {Workshift} from "../models/workshift";
import {Search} from "../commons/models/search";
import {HttpClient} from "@angular/common/http";
import {WORKSHIFTS_URL} from "../constants/constants";
import {Injectable} from "@angular/core";

@Injectable()
export class WorkshiftsService extends AbstractService<Workshift> {

    constructor(protected http: HttpClient) {
        super(WORKSHIFTS_URL, http);
    }

    buildSearch() {
        let search = new Search<Workshift>(Workshift);
        search.pageSize = 10;
        this.search = search;
        return search;
    }

    getId(element: Workshift) {
        return element.uuid;
    }


}