import { TestBed } from '@angular/core/testing';

import { StdTscSecTasksService } from './std-tsc-sec-tasks.service';

describe('StdTscSecTasksService', () => {
  let service: StdTscSecTasksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StdTscSecTasksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
