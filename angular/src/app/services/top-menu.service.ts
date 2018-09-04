import {EventEmitter, Injectable} from '@angular/core';

@Injectable()
export class TopMenuService {

	public activateMenuEvent: EventEmitter<string> = new EventEmitter<string>();

	constructor() {
	}

	public activateMenu(menu: string) {
		this.activateMenuEvent.emit(menu);
	}

}
