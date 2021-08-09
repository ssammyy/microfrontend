import { TestBed } from '@angular/core/testing';

import { StdIntStandardService } from './std-int-standard.service';

describe('StdIntStandardService', () => {
  let service: StdIntStandardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StdIntStandardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
