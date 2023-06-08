import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComplaintPlanListComponent } from './complaint-plan-list.component';

describe('ComplaintPlanListComponent', () => {
  let component: ComplaintPlanListComponent;
  let fixture: ComponentFixture<ComplaintPlanListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplaintPlanListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplaintPlanListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
