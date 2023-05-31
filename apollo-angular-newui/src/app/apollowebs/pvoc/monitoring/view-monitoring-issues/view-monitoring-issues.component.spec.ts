import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewMonitoringIssuesComponent} from './view-monitoring-issues.component';

describe('ViewMonitoringIssuesComponent', () => {
  let component: ViewMonitoringIssuesComponent;
  let fixture: ComponentFixture<ViewMonitoringIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewMonitoringIssuesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewMonitoringIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
