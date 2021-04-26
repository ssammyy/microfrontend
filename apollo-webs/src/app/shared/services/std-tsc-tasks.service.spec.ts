import { TestBed } from '@angular/core/testing';

import { StdTscTasksService } from './std-tsc-tasks.service';

describe('StdTscTasksService', () => {
  let service: StdTscTasksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StdTscTasksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
