import { TestBed } from '@angular/core/testing';

import { MembershipToTcService } from './membership-to-tc.service';

describe('MembershipToTcService', () => {
  let service: MembershipToTcService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MembershipToTcService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
