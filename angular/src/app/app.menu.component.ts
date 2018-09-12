import { Component, forwardRef, Inject, Input, OnInit } from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/primeng';
import { AppComponent } from './app.component';
import {Utente} from "./commons/models/utente";

@Component({
    selector: 'app-menu',
    template: `
        <ul app-submenu [item]="model" root="true" class="ultima-menu ultima-main-menu clearfix" [reset]="reset"></ul>
    `
})
export class AppMenuComponent implements OnInit {

    @Input() reset: boolean;

    @Input() utente: Utente;

    model: MenuItem[];

    constructor( @Inject(forwardRef(() => AppComponent)) public app: AppComponent) {
    }

    ngOnInit() {

        this.buildMenu();
    }

    private buildMenu() {
        this.model = [];

        let menuItem: MenuItem = null;
        let menuSubItem: MenuItem = null;
        let menuSubItems: MenuItem[];

        {
            menuItem = { label: 'Home', icon: 'home', routerLink: ['/home'] };
            this.model.push(menuItem);
        }

        {
            menuSubItems = [];
            menuItem = { label: 'Configurazione Aziende', icon: 'settings', items: [] };
            this.model.push(menuItem);
            menuSubItem = { label: 'lista', icon: 'list', routerLink: ['/companyconfigurations/list'] };
            menuSubItems.push(menuSubItem);

            menuItem.items = menuSubItems;
        }

        {
            menuSubItems = [];
            menuItem = { label: 'Lingue', icon: 'language', items: [] };
            this.model.push(menuItem);
            menuSubItem = { label: 'lista', icon: 'list', routerLink: ['/languages/list'] };
            menuSubItems.push(menuSubItem);

            menuItem.items = menuSubItems;
        }
        {
            menuSubItems = [];
            menuItem = { label: 'Location', icon: 'location_on', items: [] };
            this.model.push(menuItem);
            menuSubItem = { label: 'lista', icon: 'list', routerLink: ['/locations/list'] };
            menuSubItems.push(menuSubItem);

            menuItem.items = menuSubItems;
        }

        {
            menuSubItems = [];
            menuItem = { label: 'Notifiche', icon: 'send', items: [] };
            this.model.push(menuItem);
            menuSubItem = { label: 'lista', icon: 'list', routerLink: ['/notifications/list'] };
            menuSubItems.push(menuSubItem);

            menuItem.items = menuSubItems;
        }
        {
            menuSubItems = [];
            menuItem = { label: 'Operazioni', icon: 'edit', items: [] };
            this.model.push(menuItem);
            menuSubItem = { label: 'lista', icon: 'list', routerLink: ['/operations/list'] };
            menuSubItems.push(menuSubItem);

            menuItem.items = menuSubItems;
        }
        {
            menuSubItems = [];
            menuItem = { label: 'Turni', icon: 'group_work', items: [] };
            this.model.push(menuItem);
            menuSubItem = { label: 'lista', icon: 'list', routerLink: ['/workshifts/list'] };
            menuSubItems.push(menuSubItem);

            menuItem.items = menuSubItems;
        }
        {
            menuItem = { label: 'Logout', icon: 'power_settings_new', routerLink: ['/logout-cas'] };
            this.model.push(menuItem);
        }

    }

    changeTheme(theme) {
        const themeLink: HTMLLinkElement = <HTMLLinkElement>document.getElementById('theme-css');
        const layoutLink: HTMLLinkElement = <HTMLLinkElement>document.getElementById('layout-css');

        themeLink.href = 'assets/theme/theme-' + theme + '.css';
        layoutLink.href = 'assets/layout/css/layout-' + theme + '.css';
    }
}

@Component({
    selector: '[app-submenu]',
    template: `
        <ng-template ngFor let-child let-i="index" [ngForOf]="(root ? item : item.items)">
            <ng-container *ngIf="!child.hidden">
                <li [ngClass]="{'active-menuitem': isActive(i)}" >
                    <a [href]="child.url||'#'" (click)="itemClick($event,child,i)" class="ripplelink" *ngIf="!child.routerLink">
                        <i class="material-icons">{{child.icon}}</i>
                        <span>{{child.label}}</span>
                        <i class="material-icons" *ngIf="child.items">keyboard_arrow_down</i>
                    </a>

                    <a (click)="itemClick($event,child,i)" class="ripplelink" *ngIf="child.routerLink"
                        [routerLink]="child.routerLink" routerLinkActive="active-menuitem-routerlink" [routerLinkActiveOptions]="{exact: true}">
                        <i class="material-icons">{{child.icon}}</i>
                        <span>{{child.label}}</span>
                        <i class="material-icons" *ngIf="child.items">keyboard_arrow_down</i>
                    </a>
                    <ul app-submenu [item]="child" *ngIf="child.items" [@children]="isActive(i) ? 'visible' : 'hidden'"></ul>
                </li>
            </ng-container>
        </ng-template>
    `,
    animations: [
        trigger('children', [
            state('hidden', style({
                height: '0px'
            })),
            state('visible', style({
                height: '*'
            })),
            transition('visible => hidden', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)')),
            transition('hidden => visible', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
        ])
    ]
})
export class AppSubMenu {

    @Input() item: MenuItem;

    @Input() root: boolean;

    _reset: boolean;

    activeIndex: number;

    constructor( @Inject(forwardRef(() => AppComponent)) public app: AppComponent, public router: Router, public location: Location) {
    }

    itemClick(event: Event, item: MenuItem, index: number) {
        if (item.disabled) {
            event.preventDefault();
            return true;
        }

        this.activeIndex = (this.activeIndex === index) ? null : index;

        // execute command
        if (item.command) {
            item.command({
                originalEvent: event,
                item: item
            });
        }

        // prevent hash change
        if (item.items || (!item.url && !item.routerLink)) {
            event.preventDefault();
        }
        // hide menu
        if (!item.items) {
            if (this.app.isHorizontal()) {
                this.app.resetMenu = true;
            } else {
                this.app.resetMenu = false;
            }
            this.app.overlayMenuActive = false;
            this.app.staticMenuMobileActive = false;
        }

    }

    isActive(index: number): boolean {
        return this.activeIndex === index;
    }

    @Input() get reset(): boolean {
        return this._reset;
    }

    set reset(val: boolean) {
        this._reset = val;

        if (this._reset && this.app.isHorizontal()) {
            this.activeIndex = null;
        }
    }
}