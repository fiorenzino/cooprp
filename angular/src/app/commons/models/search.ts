export class Search<T> {
    from: T;
    to: T;
    like: T;
    obj: T;
    not: T;

    startRow = 0;
    pageSize = 5;
    orderBy: string;

    constructor(TCreator: { new (): T; }) {
        this.from = new TCreator();
        this.to = new TCreator();
        this.like = new TCreator();
        this.obj = new TCreator();
        this.not = new TCreator();
    }

}
