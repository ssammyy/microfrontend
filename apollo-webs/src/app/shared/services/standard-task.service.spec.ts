import { TestBed } from '@angular/core/testing';

import { StandardTaskService } from './standard-task.service';

describe('StandardTaskService', () => {
  let service: StandardTaskService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StandardTaskService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
