import {ComponentFixture, TestBed} from '@angular/core/testing';

import {TimelineIssuesComponent} from './timeline-issues.component';

describe('TimelineIssuesComponent', () => {
  let component: TimelineIssuesComponent;
  let fixture: ComponentFixture<TimelineIssuesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TimelineIssuesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimelineIssuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
