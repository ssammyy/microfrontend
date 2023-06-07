import { TestBed } from '@angular/core/testing';

import { NepPointService } from './nep-point.service';

describe('NepPointService', () => {
  let service: NepPointService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NepPointService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
