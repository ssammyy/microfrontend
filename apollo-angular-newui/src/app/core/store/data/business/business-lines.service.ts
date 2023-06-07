import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from "@ngrx/data";
import {BusinessLines} from "./business-lines.model";

@Injectable({
  providedIn: 'root'
})
export class BusinessLinesService extends EntityCollectionServiceBase<BusinessLines> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('BusinessLines', serviceElementsFactory);
  }
}
