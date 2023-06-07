import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from '@ngrx/data';
import {Region} from './region.model';

@Injectable({
  providedIn: 'root'
})
export class RegionService extends EntityCollectionServiceBase<Region> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Region', serviceElementsFactory);
  }
}
