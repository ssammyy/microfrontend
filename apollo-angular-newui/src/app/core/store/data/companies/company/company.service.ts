import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from "@ngrx/data";
import {Company} from "./company.model";

@Injectable({
  providedIn: 'root'
})
export class CompanyService extends EntityCollectionServiceBase<Company> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Company', serviceElementsFactory);
  }
}
