import {Component, OnInit} from "@angular/core";
import {APP_VERSION} from "./constants/constants";
import * as moment from "moment/moment";

@Component({
    selector: 'app-footer',
    template: `
        <div class="footer">
            <div class="card clearfix CardNoMarginBottom">
                <span class="footer-text-left">FATTURE PA - {{year}} v. {{appVersion}}</span>
                <span class="footer-text-right"><span class="ui-icon ui-icon-copyright"></span>  <span>www.coopservice.it</span></span>
            </div>
        </div>
    `
})
export class AppFooter implements OnInit {

    public appVersion: string = APP_VERSION;
    public year: string;

    ngOnInit() {
        this.year = moment().format('YYYY');
    }

}
