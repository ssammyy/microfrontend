import { TestBed } from '@angular/core/testing';

import { AdoptionOfEaStdsService } from './adoption-of-ea-stds.service';

describe('AdoptionOfEaStdsService', () => {
  let service: AdoptionOfEaStdsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdoptionOfEaStdsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
