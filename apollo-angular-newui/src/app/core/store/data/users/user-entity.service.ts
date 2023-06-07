import { Injectable } from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from '@ngrx/data';
import {UserEntityDto} from './user.model';

@Injectable({
  providedIn: 'root'
})
export class UserEntityService  extends EntityCollectionServiceBase<UserEntityDto> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('UserEntityDto', serviceElementsFactory);
  }
}
