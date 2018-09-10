import {AbstractService} from "../commons/abstract-service";
import {Search} from "../commons/models/search";
import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {CompanyConfiguration} from "../models/companyConfiguration";
import {COMPANY_CONFIGURATIONS_URL} from "../constants/constants";

@Injectable()
export class CompanyConfigurationsService extends AbstractService<CompanyConfiguration> {

    constructor(protected http: HttpClient) {
        super(COMPANY_CONFIGURATIONS_URL, http);
    }

    buildSearch() {
        let search = new Search<CompanyConfiguration>(CompanyConfiguration);
        search.pageSize = 10;
        this.search = search;
        return search;
    }

    getId(element: CompanyConfiguration) {
        return element.uuid;
    }


}