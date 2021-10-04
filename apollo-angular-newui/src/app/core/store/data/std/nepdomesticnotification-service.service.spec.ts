import { TestBed } from '@angular/core/testing';

import { NepdomesticnotificationServiceService } from './nepdomesticnotification-service.service';

describe('NepdomesticnotificationServiceService', () => {
  let service: NepdomesticnotificationServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NepdomesticnotificationServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
