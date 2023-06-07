import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkPlanBatchListComponent } from './work-plan-batch-list.component';

describe('WorkplanBatchListComponent', () => {
  let component: WorkPlanBatchListComponent;
  let fixture: ComponentFixture<WorkPlanBatchListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkPlanBatchListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkPlanBatchListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
