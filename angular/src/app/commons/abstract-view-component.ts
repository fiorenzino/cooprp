import {AbstractService} from "./abstract-service";
import {ActivatedRoute, Router} from "@angular/router";
import {Message} from "primeng/primeng";
import {OnInit} from "@angular/core";
import {manageErrorCode} from "../shared/manageErrorCode";

export abstract class AbstractViewComponent<T> implements OnInit {

	public msgs: Message[] = [];
	public gmsgs: Message[] = [];

	public editMode: boolean = false;
	public element: T = null;

	constructor(protected router: Router,
				protected route: ActivatedRoute,
				public service: AbstractService<T>) {
	}

	ngOnInit() {
		let id: string = this.route.snapshot.params['id'];
		if (id) {
			this.editMode = true;
			this.service.find(id)
				.subscribe(element => {
						this.element = this.toRec(element);
						this.postFind();
					},
					error => {
						manageErrorCode('Errore nel caricamento dei dati', this.msgs, this.router);
					}
				);
		} else {
			this.addError("Errore nel caricamento dei dati.");
		}
	}

	postFind() {
	}

	goToList() {
		this.clearMsgs();
		this.navigateToList();
	}

	public clearMsgs() {
		this.msgs = [];
	}

	public addInfo(message: string) {
		this.msgs.push({severity: 'info', summary: 'Informazioni: ', detail: message});
	}

	public addWarn(message: string) {
		this.msgs.push({severity: 'warn', summary: 'Attenzione: ', detail: message});
	}

	public addError(message: string) {
		this.msgs.push({severity: 'error', summary: 'Errore: ', detail: message});
	}

	abstract getId();

	abstract navigateToList();

	abstract toRec(obj: any): T;
}
