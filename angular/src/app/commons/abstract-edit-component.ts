import { AbstractService } from './abstract-service';
import { ActivatedRoute, Router } from '@angular/router';
import { Message } from 'primeng/primeng';
import { OnInit } from '@angular/core';
import { manageErrorCode } from '../shared/manageErrorCode';

export abstract class AbstractEditComponent<T> implements OnInit {
  public msgs: Message[] = [];
  public gmsgs: Message[] = [];

  public editMode = false;
  public element: T = null;

  public lang_it = {
    closeText: 'Chiudi',
    prevText: '&#x3C;Prec',
    nextText: 'Succ&#x3E;',
    currentText: 'Oggi',
    monthNames: [
      'Gennaio',
      'Febbraio',
      'Marzo',
      'Aprile',
      'Maggio',
      'Giugno',
      'Luglio',
      'Agosto',
      'Settembre',
      'Ottobre',
      'Novembre',
      'Dicembre'
    ],
    monthNamesShort: [
      'Gen',
      'Feb',
      'Mar',
      'Apr',
      'Mag',
      'Giu',
      'Lug',
      'Ago',
      'Set',
      'Ott',
      'Nov',
      'Dic'
    ],
    dayNames: [
      'Domenica',
      'Lunedì',
      'Martedì',
      'Mercoledì',
      'Giovedì',
      'Venerdì',
      'Sabato'
    ],
    dayNamesShort: ['Dom', 'Lun', 'Mar', 'Mer', 'Gio', 'Ven', 'Sab'],
    dayNamesMin: ['Do', 'Lu', 'Ma', 'Me', 'Gi', 'Ve', 'Sa'],
    weekHeader: 'Sm',
    dateFormat: 'dd/mm/yy',
    firstDayOfWeek: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: ''
  };

  constructor(
    protected router: Router,
    protected route: ActivatedRoute,
    public service: AbstractService<T>
  ) {}

  ngOnInit() {
    const id: string = this.route.snapshot.params['id'];
    if (id) {
      this.editMode = true;
      this.service.find(id).subscribe(
        element => {
          this.element = this.toRec(element);
          this.postFind();
        },
        error => {
          manageErrorCode(
            'Errore nel caricamento dei dati',
            this.msgs,
            this.router
          );
        }
      );
    } else {
      this.editMode = false;
      this.element = this.createInstance();
      this.postCreate();
    }
  }

  postCreate() {}

  postFind() {}

  preSave(): boolean {
    return true;
  }

  preUpdate(): boolean {
    return true;
  }

  postSave() {}

  postUpdate() {}

  postDelete() {}

  save() {
    this.clearMsgs();
    this.editMode = false;
    if (!this.preSave()) {
      return;
    }
    this.service.persist(this.element).subscribe(
      element => {
        this.addInfo('Salvataggio completato con successo. ');
        this.element = this.toRec(element);
        this.postSave();
        this.navigateAfterSave();
      },
      error => {
        this.saveError();
        manageErrorCode(
          'Impossibile completare il salvataggio',
          this.msgs,
          this.router
        );
      }
    );
  }

  saveError() {}

  update() {
    this.clearMsgs();
    this.editMode = false;
    if (!this.preUpdate()) {
      return;
    }
    this.service.update(this.element).subscribe(
      element => {
        this.addInfo('Modifica completata con successo. ');
        this.element = this.toRec(element);
        this.postUpdate();
        this.navigateAfterUpdate();
      },
      error => {
        this.saveError();
        manageErrorCode(
          'Impossibile completare la modifica',
          this.msgs,
          this.router
        );
      }
    );
  }

  delete() {
    this.clearMsgs();
    this.editMode = false;
    this.service.delete(this.getId()).subscribe(
      element => {
        this.postDelete();
        this.navigateAfterDelete();
        this.addInfo('Eliminazione completata con successo. ');
      },
      error => {
        manageErrorCode(
          'Impossibile completare la eliminazione',
          this.msgs,
          this.router
        );
      }
    );
  }

  goToList() {
    this.clearMsgs();
    this.navigateToList();
  }

  public isEditMode(): boolean {
    return this.editMode;
  }

  public clearMsgs() {
    this.msgs = [];
  }

  public addInfo(message: string) {
    this.msgs.push({
      severity: 'info',
      summary: 'Informazioni: ',
      detail: message
    });
  }

  public addWarn(message: string) {
    this.msgs.push({
      severity: 'warn',
      summary: 'Attenzione: ',
      detail: message
    });
  }

  public addError(message: string) {
    this.msgs.push({ severity: 'error', summary: 'Errore: ', detail: message });
  }

  abstract createInstance(): T;

  abstract getId();

  abstract navigateAfterDelete();

  abstract navigateAfterSave();

  abstract navigateAfterUpdate();

  abstract navigateToList();

  abstract toRec(obj: any): T;
}
