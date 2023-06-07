import { TestBed } from '@angular/core/testing';

import { NepdomesticnotificationService } from './nepdomesticnotification.service';

describe('NepdomesticnotificationService', () => {
  let service: NepdomesticnotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NepdomesticnotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
