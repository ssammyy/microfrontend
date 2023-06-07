import { TestBed } from '@angular/core/testing';

import { SchemeMembershipService } from './scheme-membership.service';

describe('SchemeMembershipService', () => {
  let service: SchemeMembershipService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SchemeMembershipService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
