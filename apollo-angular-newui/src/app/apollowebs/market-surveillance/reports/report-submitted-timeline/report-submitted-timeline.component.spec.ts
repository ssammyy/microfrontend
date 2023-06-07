import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportSubmittedTimelineComponent } from './report-submitted-timeline.component';

describe('ReportSubmittedTimelineComponent', () => {
  let component: ReportSubmittedTimelineComponent;
  let fixture: ComponentFixture<ReportSubmittedTimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportSubmittedTimelineComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportSubmittedTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
