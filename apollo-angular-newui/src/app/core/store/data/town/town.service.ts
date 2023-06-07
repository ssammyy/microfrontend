import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from "@ngrx/data";
import {Town} from "./town.model";

@Injectable({
  providedIn: 'root'
})
export class TownService extends EntityCollectionServiceBase<Town> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Town', serviceElementsFactory);
  }
}
