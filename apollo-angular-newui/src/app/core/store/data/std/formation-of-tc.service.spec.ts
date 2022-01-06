import { TestBed } from '@angular/core/testing';

import { FormationOfTcService } from './formation-of-tc.service';

describe('FormationOfTcService', () => {
  let service: FormationOfTcService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormationOfTcService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
