import { TestBed } from '@angular/core/testing';

import { QaInternalService } from './qa-internal.service';

describe('QaInternalService', () => {
  let service: QaInternalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QaInternalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
