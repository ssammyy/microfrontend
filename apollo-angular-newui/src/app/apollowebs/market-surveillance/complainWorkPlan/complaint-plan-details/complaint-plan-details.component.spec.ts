import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComplaintPlanDetailsComponent } from './complaint-plan-details.component';

describe('ComplaintPlanDetailsComponent', () => {
  let component: ComplaintPlanDetailsComponent;
  let fixture: ComponentFixture<ComplaintPlanDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplaintPlanDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplaintPlanDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
