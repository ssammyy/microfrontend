import {TestBed} from '@angular/core/testing';

import {BusinessLinesService} from './business-lines.service';

describe('BusinessLinesService', () => {
  let service: BusinessLinesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessLinesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
