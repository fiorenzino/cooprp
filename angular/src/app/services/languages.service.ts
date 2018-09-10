import {AbstractService} from "../commons/abstract-service";
import {Language} from "../modules/language";
import {Search} from "../commons/models/search";
import {HttpClient} from "@angular/common/http";
import {LANGUAGES_URL} from "../constants/constants";
import {Injectable} from "@angular/core";

@Injectable()
export class LanguagesService extends AbstractService<Language> {

    constructor(protected http: HttpClient) {
        super(LANGUAGES_URL, http);
    }

    buildSearch() {
        let search = new Search<Language>(Language);
        search.pageSize = 10;
        this.search = search;
        return search;
    }

    getId(element: Language) {
        return element.uuid;
    }


}