import {AbstractService} from "../commons/abstract-service";
import {Operation} from "../models/operation";
import {Search} from "../commons/models/search";
import {HttpClient} from "@angular/common/http";
import {OPERATIONS_URL} from "../constants/constants";
import {Injectable} from "@angular/core";

@Injectable()
export class OperationsService extends AbstractService<Operation> {

    constructor(protected http: HttpClient) {
        super(OPERATIONS_URL, http);
    }

    buildSearch() {
        let search = new Search<Operation>(Operation);
        search.pageSize = 10;
        this.search = search;
        return search;
    }

    getId(element: Operation) {
        return element.uuid;
    }


}