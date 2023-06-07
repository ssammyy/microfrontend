import { TestBed } from '@angular/core/testing';

import { LevyService } from './levy.service';

describe('LevyService', () => {
  let service: LevyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LevyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
