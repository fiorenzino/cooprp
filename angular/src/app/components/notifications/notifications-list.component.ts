import {Component, OnInit} from "@angular/core";
import {AbstractListComponent} from "../../commons/abstract-list-component";
import {Notification} from "../../models/notification";
import {Router} from "@angular/router";
import {NotificationsService} from "../../services/notifications.service";
import {CompanyConfiguration} from "../../models/companyConfiguration";

@Component({
    templateUrl: './notifications-list.component.html',
})
export class NotificationsListComponent extends AbstractListComponent<Notification> implements OnInit {

    constructor(protected router: Router, public service: NotificationsService) {
        super(router, service);
    }


    ngOnInit(): void {

        super.ngOnInit();
    }

    getId() {
        return this.element.uuid;
    }

    toRec(obj: any): Notification {
        return obj;
    }

    public view(element: Notification) {
        this.element = element;
        this.router.navigate(['/notifications/view', this.getId()]);
    }

    public edit(element: Notification) {
        this.element = element;
        this.router.navigate(['/notifications/edit', this.getId()]);
    }


    newElement(): Notification {
        return new Notification();
    }
}