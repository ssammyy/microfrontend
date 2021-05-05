import { TestBed } from '@angular/core/testing';

import { CommitteeDraftService } from './committee-draft.service';

describe('CommitteeDraftService', () => {
  let service: CommitteeDraftService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommitteeDraftService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
