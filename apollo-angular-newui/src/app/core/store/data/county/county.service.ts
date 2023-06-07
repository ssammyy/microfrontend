import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from "@ngrx/data";
import {County} from "./county.model";

@Injectable({
  providedIn: 'root'
})
export class CountyService extends EntityCollectionServiceBase<County> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('County', serviceElementsFactory);
  }
}
