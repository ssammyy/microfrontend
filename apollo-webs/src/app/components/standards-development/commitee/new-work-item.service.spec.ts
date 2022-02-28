import { TestBed } from '@angular/core/testing';

import { NewWorkItemService } from './new-work-item.service';

describe('NewWorkItemService', () => {
  let service: NewWorkItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NewWorkItemService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
