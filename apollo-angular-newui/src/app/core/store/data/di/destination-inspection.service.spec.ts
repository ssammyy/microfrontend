import { TestBed } from '@angular/core/testing';

import { DestinationInspectionService } from './destination-inspection.service';

describe('DestinationInspectionService', () => {
  let service: DestinationInspectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DestinationInspectionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
