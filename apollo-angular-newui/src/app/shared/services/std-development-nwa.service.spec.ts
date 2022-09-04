import { TestBed } from '@angular/core/testing';

import { StdDevelopmentNwaService } from './std-development-nwa.service';

describe('StdDevelopmentNwaService', () => {
  let service: StdDevelopmentNwaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StdDevelopmentNwaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
