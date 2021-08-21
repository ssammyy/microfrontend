import { TestBed } from '@angular/core/testing';

import { StdComStandardService } from './std-com-standard.service';

describe('StdComStandardService', () => {
  let service: StdComStandardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StdComStandardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
