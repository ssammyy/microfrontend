import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from "@ngrx/data";
import {Branches} from "./branches.model";

@Injectable({
  providedIn: 'root'
})
export class BranchesService extends EntityCollectionServiceBase<Branches> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('Branches', serviceElementsFactory);
  }
}
