import {Component, OnInit} from '@angular/core';
import {TopMenuService} from "../../services/top-menu.service";

@Component({
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

	constructor(private topMenuService: TopMenuService) {
	}

	ngOnInit() {
		this.topMenuService.activateMenu('home');
	}

}
