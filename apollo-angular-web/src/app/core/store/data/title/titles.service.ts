import { Injectable } from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from '@ngrx/data';
import {Titles} from './titles.model';

@Injectable({
  providedIn: 'root'
})
export class TitlesService extends EntityCollectionServiceBase<Titles> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Titles', serviceElementsFactory);
  }
}
