import { TestBed } from '@angular/core/testing';

import { StandardDevelopmentService } from './standard-development.service';

describe('StandardDevelopmentService', () => {
  let service: StandardDevelopmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StandardDevelopmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
