import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkPlanListComponent } from './work-plan-list.component';

describe('WorkPlanListComponent', () => {
  let component: WorkPlanListComponent;
  let fixture: ComponentFixture<WorkPlanListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkPlanListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkPlanListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
