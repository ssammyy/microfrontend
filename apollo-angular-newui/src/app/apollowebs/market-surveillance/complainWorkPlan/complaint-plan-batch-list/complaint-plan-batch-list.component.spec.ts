import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComplaintPlanBatchListComponent } from './complaint-plan-batch-list.component';

describe('ComplaintPlanBatchListComponent', () => {
  let component: ComplaintPlanBatchListComponent;
  let fixture: ComponentFixture<ComplaintPlanBatchListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplaintPlanBatchListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplaintPlanBatchListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
