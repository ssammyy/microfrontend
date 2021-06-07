import { TestBed } from '@angular/core/testing';

import { PublicReviewService } from './public-review.service';

describe('PublicReviewService', () => {
  let service: PublicReviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicReviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
