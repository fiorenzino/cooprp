import {Component, OnInit} from '@angular/core';
import {Utente} from "../../commons/models/utente";
import {ActivatedRoute} from "@angular/router";

@Component({
	templateUrl: './main-app.component.html',
	styleUrls: ['./main-app.component.css']
})
export class MainAppComponent implements OnInit {

	private utente: Utente;

	constructor(private route: ActivatedRoute) {
	}

	ngOnInit() {
		this.route.data.subscribe((data: { utente: Utente }) => {
			if (data.utente) {
				this.utente = data.utente;
			}
		});
	}

}
