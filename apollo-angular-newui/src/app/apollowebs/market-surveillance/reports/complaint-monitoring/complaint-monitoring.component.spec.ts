import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComplaintMonitoringComponent } from './complaint-monitoring.component';

describe('ComplaintMonitoringComponent', () => {
  let component: ComplaintMonitoringComponent;
  let fixture: ComponentFixture<ComplaintMonitoringComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplaintMonitoringComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplaintMonitoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
