import {AbstractService} from "../commons/abstract-service";
import {Notification} from "../models/notification";
import {Search} from "../commons/models/search";
import {HttpClient} from "@angular/common/http";
import {NOTIFICATIONS_URL} from "../constants/constants";
import {Injectable} from "@angular/core";

@Injectable()
export class NotificationsService extends AbstractService<Notification> {

    constructor(protected http: HttpClient) {
        super(NOTIFICATIONS_URL, http);
    }

    buildSearch() {
        let search = new Search<Notification>(Notification);
        search.pageSize = 10;
        this.search = search;
        return search;
    }

    getId(element: Notification) {
        return element.uuid;
    }


}