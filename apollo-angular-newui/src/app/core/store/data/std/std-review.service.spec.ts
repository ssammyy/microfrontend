import { TestBed } from '@angular/core/testing';

import { StdReviewService } from './std-review.service';

describe('StdReviewService', () => {
  let service: StdReviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StdReviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
