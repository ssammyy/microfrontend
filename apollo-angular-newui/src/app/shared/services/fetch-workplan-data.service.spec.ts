import {TestBed} from '@angular/core/testing';

import {FetchWorkplanDataService} from './fetch-workplan-data.service';

describe('FetchWorkplanDataService', () => {
  let service: FetchWorkplanDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FetchWorkplanDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
