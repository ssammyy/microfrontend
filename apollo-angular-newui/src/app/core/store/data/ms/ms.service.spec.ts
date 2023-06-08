import { TestBed } from '@angular/core/testing';

import { MsService } from './ms.service';

describe('MsService', () => {
  let service: MsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
