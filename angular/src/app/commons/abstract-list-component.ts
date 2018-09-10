import { DataTable, LazyLoadEvent, Message } from 'primeng/primeng';
import { AbstractService } from './abstract-service';
import { Router } from '@angular/router';
import { OnInit } from '@angular/core';
import { Table } from 'primeng/table';
import {manageErrorCode} from "../shared/manageErrorCode";

export abstract class AbstractListComponent<T> implements OnInit {
  msgs: Message[] = [];

  element: T = null;
  model: T[] = [];
  listSize: number;

  protected firstReload: boolean;

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
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: ''
  };

  constructor(protected router: Router, public service: AbstractService<T>) {}

  ngOnInit() {
    this.service.buildSearch();
    this.firstReload = true;
  }

  public loaddata(firstReload: boolean, datatable?: any) {
    this.preLoaddata();
    this.service.getList().subscribe(
      model => {
        this.model = [];
        model.forEach(rec => {
          this.model.push(this.toRec(rec));
        });
        this.listSize = this.service.listSize;
        this.postLoaddata();
      },
      error => {
        this.errorLoaddata(error);
        manageErrorCode(error, this.msgs, this.router);
      }
    );
  }

  public preLoaddata() {}

  public postLoaddata() {}

  public errorLoaddata(error) {}

  public lazyLoad(event: LazyLoadEvent, datatable?: any) {
    if (!this.firstReload) {
      this.service.search.startRow = event.first;
    }
    this.service.search.pageSize = event.rows;
    this.preLoad(event, datatable);
    this.loaddata(this.firstReload, datatable);
    if (this.firstReload) {
      this.firstReload = false;
    }
  }

  protected preLoad(event: LazyLoadEvent, datatable?: any) {}

  public refresh(datatable: Table) {
    this.clearMsgs();
    datatable.reset();
  }

  public reload(datatable: Table) {
    this.service.search.startRow = 0;
    this.refresh(datatable);
  }

  public reset(datatable: Table) {
    this.service.buildSearch();
    this.refresh(datatable);
  }

  public newElement(): T {
    throw new Error('override this');
  }

  public onRowSelect(event: T, focusable: any) {
    this.element = event;
    if (focusable) {
      focusable.focus();
    }
  }

  public getNavigateOnView() {
    return null;
  }

  public getNavigateOnEdit() {
    return null;
  }

  public postSave() {}

  public postUpdate() {}

  public postDelete() {}

  public save() {
    this.clearMsgs();
    this.service.persist(this.element).subscribe(
      element => {
        this.addInfo('Salvataggio completato con successo. ');
        this.element = this.newElement();
        this.loaddata(false);
        this.postSave();
      },
      error => {
        manageErrorCode(
          'Impossibile completare il salvataggio. Si prega di riprovare',
          this.msgs,
          this.router
        );
      }
    );
  }

  public undo(focusable: any) {
    this.clearMsgs();
    this.element = this.newElement();
    if (focusable) {
      focusable.focus();
    }
  }

  public delete(element: T) {
    this.clearMsgs();
    this.service.delete(this.getId()).subscribe(
      result => {
        this.addInfo('Eliminazione completata con successo. ');
        this.element = this.newElement();
        this.loaddata(false);
        this.postDelete();
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

  public update() {
    this.clearMsgs();
    this.service.update(this.element).subscribe(
      element => {
        this.addInfo('Modifica completata con successo. ');
        this.element = this.newElement();
        this.loaddata(false);
        this.postUpdate();
      },
      error => {
        manageErrorCode(
          'Impossibile completare la modifica',
          this.msgs,
          this.router
        );
      }
    );
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

  public addError(error: any) {
    console.error(error);
    let message = 'Errore generico';
    if ( error ) {
      message = error;
    }
    if ( error['error'] ) {
      message = error['error'];
      if ( error['error']['msg'] ) {
        message = error['error']['msg'];
      }
    }
    this.msgs.push({ severity: 'error', summary: 'Errore: ', detail: message });
  }

  public clearMsgs() {
    this.msgs = [];
  }

  abstract getId();

  abstract toRec(obj: any): T;
}
