import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from "@ngrx/data";
import {BusinessNatures} from "./business-natures.model";

@Injectable({
  providedIn: 'root'
})
export class BusinessNaturesService extends EntityCollectionServiceBase<BusinessNatures> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('BusinessNatures', serviceElementsFactory);
  }
}
