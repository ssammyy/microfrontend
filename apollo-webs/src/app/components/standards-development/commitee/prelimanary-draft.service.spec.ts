import { TestBed } from '@angular/core/testing';

import { PreliminaryDraftService } from './preliminary-draft.service';

describe('PrelimanaryDraftService', () => {
  let service: PreliminaryDraftService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PreliminaryDraftService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
