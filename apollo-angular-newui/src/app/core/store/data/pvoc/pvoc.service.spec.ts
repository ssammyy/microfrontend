import { TestBed } from '@angular/core/testing';

import { PVOCService } from './pvoc.service';

describe('PVOCService', () => {
  let service: PVOCService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PVOCService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
