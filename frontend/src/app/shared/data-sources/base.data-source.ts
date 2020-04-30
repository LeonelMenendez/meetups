import { CollectionViewer } from '@angular/cdk/collections';
import { DataSource } from '@angular/cdk/table';
import { BehaviorSubject, Observable } from 'rxjs';
import { Model } from 'src/app/shared/models/model';

export abstract class BaseDataSource<T extends Model> extends DataSource<T> {
  sourceSubject = new BehaviorSubject<T[]>([]);
  loadingSubject = new BehaviorSubject<boolean>(false);
  loading$ = this.loadingSubject.asObservable();

  constructor() {
    super();
  }

  abstract load(service: any): void;

  connect(collectionViewer: CollectionViewer): Observable<T[]> {
    return this.sourceSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.sourceSubject.complete();
    this.loadingSubject.complete();
  }

  add(toAdd: T): void {
    this.sourceSubject.next(this.sourceSubject.getValue().concat([toAdd]));
  }

  update(toUpdate: T): void {
    let source = this.sourceSubject.getValue();
    source = source.map((element) => (element.id === toUpdate.id ? toUpdate : element));

    this.sourceSubject.next(source);
  }
}
