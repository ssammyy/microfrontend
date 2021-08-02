import { TestBed } from '@angular/core/testing';

import { StdNwaService } from './std-nwa.service';

describe('StdNwaService', () => {
  let service: StdNwaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StdNwaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
