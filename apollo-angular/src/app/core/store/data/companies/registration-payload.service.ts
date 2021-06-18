import {Injectable} from '@angular/core';
import {EntityCollectionServiceBase, EntityCollectionServiceElementsFactory} from "@ngrx/data";
import {BrsLookUpRequest, RegistrationPayload, SendTokenToPhone, ValidateTokenAndPhone} from "./company.model";

@Injectable({
  providedIn: 'root'
})
export class RegistrationPayloadService extends EntityCollectionServiceBase<RegistrationPayload> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('RegistrationPayload', serviceElementsFactory);
  }
}

@Injectable({
  providedIn: 'root'
})
export class BrsLookUpRequestService extends EntityCollectionServiceBase<BrsLookUpRequest> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('BrsLookUpRequest', serviceElementsFactory);
  }
}

@Injectable({
  providedIn: 'root'
})
export class SendTokenToPhoneService extends EntityCollectionServiceBase<SendTokenToPhone> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('SendTokenToPhone', serviceElementsFactory);
  }
}

@Injectable({
  providedIn: 'root'
})
export class ValidateTokenAndPhoneService extends EntityCollectionServiceBase<ValidateTokenAndPhone> {
  constructor(serviceElementsFactory: EntityCollectionServiceElementsFactory) {
    super('ValidateTokenAndPhone', serviceElementsFactory);
  }
}
