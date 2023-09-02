import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {Observable} from 'rxjs';
import {Store} from '@ngrx/store';
import {AppState} from '../../app-shell.module';
import {NGXLogger} from 'ngx-logger';
import {fitnessDataListElements} from '../../modules/fitness/selector';
import {map} from 'rxjs/operators';
import {Injectable} from '@angular/core';
import { FitnessDataListElement } from 'build/generated';


@Injectable()
export class FitSourcesDataSource extends DataSource<FitnessDataListElement> {

  constructor(
    private store: Store<AppState>,
    private logger: NGXLogger,
  ) {
    super();
  }

  connect(collectionViewer: CollectionViewer): Observable<Array<FitnessDataListElement>> {
    return this.store.select(fitnessDataListElements).pipe(
      // map undefined to empty array
      map(e => (e == undefined ? [] : e)),
    );
  }

  disconnect(collectionViewer: CollectionViewer): void {
    // nothing to do
  }

}
