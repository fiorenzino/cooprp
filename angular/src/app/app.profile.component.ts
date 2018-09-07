import {Component, Input} from "@angular/core";
import {trigger, state, style, transition, animate} from "@angular/animations";
import {Utente} from "./commons/models/utente";

@Component({
    selector: 'inline-profile',
    styleUrls: ['./app.profile.component.css'],
    template: `
        <div class="profile" [ngClass]="{'profile-expanded':active}">
            <div class="ui-g-12">Benvenuto</div>
            <a href="#" (click)="onClick($event)">
                <span class="profile-name">{{utente.nome}}</span>
                <i class="material-icons">keyboard_arrow_down</i>
            </a>
        </div>

        <ul class="ultima-menu profile-menu" [@menu]="active ? 'visible' : 'hidden'">
            <li role="menuitem">
                <a href="#" class="ripplelink CurDefault">
                    <i class="material-icons">security</i>
                    <span>ruolo {{utente.ruoli}}</span>
                </a>
            </li>
        </ul>
    `,
    animations: [
        trigger('menu', [
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
export class InlineProfileComponent {

    @Input() utente: Utente;

    active: boolean;

    onClick(event) {
        this.active = !this.active;
        event.preventDefault();
    }
}