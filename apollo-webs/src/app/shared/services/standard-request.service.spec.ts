import { TestBed } from '@angular/core/testing';

import { StandardRequestService } from './standard-request.service';

describe('StandardRequestService', () => {
  let service: StandardRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StandardRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
