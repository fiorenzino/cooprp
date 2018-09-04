import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {Search} from "../commons/models/search";

export abstract class AbstractService<T> {

    listSize: number;
    search: Search<T>;

    constructor(protected url: string, protected http: HttpClient) {
        this.buildSearch();
    }

    public getList(): Observable<T []> {
        let params = new HttpParams();
        params = this.applyRestrictions(params, this.search);

        return this.http.get<HttpResponse<T []>>(this.url, {
            observe: 'response',
            params: params
        }).pipe(
            map(res => {
                    this.listSize = res.headers.get('listSize') != null ? +res.headers.get('listSize') : 0;
                    const ts: any = res.body;
                    this.postList(ts);
                    return ts;
                }
            ),
            catchError(this.handleError)
        );
    }

    public getListSize(search: Search<T>): Observable<number> {
        let params = new HttpParams();
        search.startRow = 0;
        search.pageSize = 1;
        params = this.applyRestrictions(params, search);

        return this.http.get(this.url + '/listSize', {
            observe: 'response',
            params: params
        }).pipe(
            map((res: HttpResponse<number>) => {
                    return res.headers.get('listSize') != null ? +res.headers.get('listSize') : 0;
                }
            ),
            catchError(this.handleError)
        );
    }

    public size(): Observable<number> {
        let params = new HttpParams();
        this.search.startRow = 0;
        this.search.pageSize = 1;
        params = this.applyRestrictions(params, this.search);

        return this.http.get(this.url + '/listSize', {
            observe: 'response',
            params: params
        }).pipe(
            map((res: HttpResponse<number>) => {
                    return res.headers.get('listSize') != null ? +res.headers.get('listSize') : 0;
                }
            ),
            catchError(this.handleError)
        );
    }

    protected applyRestrictions(params: HttpParams, search: any, prefix?: string) {
        if (!prefix) {
            prefix = '';
        } else {
            prefix = prefix + '.';
        }
        for (const key in search) {
            if (search[key] !== null) {
                if (!(search[key] instanceof Object)) {
                    params = params.set(prefix + key, this.toQueryParam(prefix + key, search[key]));
                } else if (search[key] instanceof Date) {
                    params = params.set(prefix + key, this.toQueryParam(prefix + key, search[key]));
                } else {
                    params = this.applyRestrictions(params, search[key], prefix + key);
                }
            }
        }
        return params;
    }

    protected toQueryParam(field: string, value: any): any {
        if (value instanceof Date) {
            return (value as Date).toLocaleString('it-IT', {hour12: false});
        }
        return value;
    }

    public find(id: string): Observable<T> {
        return this.http.get<T>(this.url + '/' + id)
            .pipe(catchError(this.handleError));
    }

    public newInstance(type: { new (): T; }): T {
        return new type();
    }

    public delete(id: string): Observable<any> {
        return this.http.delete(this.url + '/' + id, {responseType: 'text'})
            .pipe(catchError(this.handleError));
    }

    public persist(element: T): Observable<T> {
        return this.http.post<T>(this.url, element)
            .pipe(catchError(this.handleError));
    }

    public update(element: T): Observable<T> {
        return this.http.put<T>(this.url + '/' + this.getId(element), element)
            .pipe(catchError(this.handleError));
    }

    public handleError(error: HttpErrorResponse): Observable<any> {
        console.error(error);
        if (error.status === 401) {
            return Observable.throw({status: error.status, error: 'Unauthorized'});
        }
        return Observable.throw(error.message /*json().msg*/ || error.error /*json().error*/ || 'Server error');
    }

    public getInstance(TCreator: { new (): T; }): T {
        return new TCreator();
    }

    public abstract getId(element: T);

    public abstract buildSearch();

    public getAllList(search?: Search<T>): Observable<T[]> {
        let params = new HttpParams();

        if (search == null) {
            search = JSON.parse(JSON.stringify(this.search));
        }
        search.pageSize = 100000;
        params = this.applyRestrictions(params, search);

        return this.http.get<HttpResponse<T []>>(this.url, {
            observe: 'response',
            params: params
        }).pipe(
            map(res => {
                    const ts: any = res.body;
                    this.postList(ts);
                    return ts;
                }
            ),
            catchError(this.handleError)
        );
    }

    protected postList(ts: T[]) {
    }

    protected postFind(t: T) {
    }

    public getUrl(link: string): string {
        return this.generateLink(link, this.search);
    }

    private generateLink(link: string, search: any, prefix?: string) {
        if (!prefix) {
            prefix = '';
        } else {
            prefix = prefix + '.';
        }
        for (const key in search) {
            if (search[key] !== null) {
                if (!(search[key] instanceof Object)) {
                    if (link.endsWith('?')) {
                        link = link + prefix + key + '=' + this.toQueryParam(prefix + key, search[key]);

                    } else {
                        link = link + '&' + prefix + key + '=' + this.toQueryParam(prefix + key, search[key]);
                    }
                } else if (search[key] instanceof Date) {
                    if (link.endsWith('?')) {
                        link = link + prefix + key + '=' + this.toQueryParam(prefix + key, search[key]);

                    } else {
                        link = link + '&' + prefix + key + '=' + this.toQueryParam(prefix + key, search[key]);
                    }
                } else {
                    // return this.generateLink(link, search[key], prefix + key);
                    link = this.generateLink(link, search[key], prefix + key);
                }
            }
        }
        return link;
    }

}
