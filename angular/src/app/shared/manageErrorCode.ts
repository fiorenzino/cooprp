import {Router} from "@angular/router";

export function manageErrorCode(error, msgs?: any[], router?: Router): void {
    if (error.status == 401) {
        if (router) {
            router.navigate(['/']);
        } else {
            if (msgs) {
                msgs.push({severity: 'error', summary: 'Errore: ', detail: 'Non autorizzato'});
            }
        }
    } else {
        if (msgs) {
            msgs.push({severity: 'error', summary: 'Errore: ', detail: error});
        }
    }
}
