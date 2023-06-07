import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkplanMonitoringToolComponent } from './workplan-monitoring-tool.component';

describe('WorkplanMonitoringToolComponent', () => {
  let component: WorkplanMonitoringToolComponent;
  let fixture: ComponentFixture<WorkplanMonitoringToolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkplanMonitoringToolComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkplanMonitoringToolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
