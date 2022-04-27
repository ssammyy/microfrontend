import { TestBed } from '@angular/core/testing';

import { BusinessNatureService } from './business-nature.service';

describe('BusinessNatureService', () => {
  let service: BusinessNatureService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessNatureService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
