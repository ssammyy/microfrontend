import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SampleSubmittedTimelineComponent } from './sample-submitted-timeline.component';

describe('SampleSubmittedTimelineComponent', () => {
  let component: SampleSubmittedTimelineComponent;
  let fixture: ComponentFixture<SampleSubmittedTimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SampleSubmittedTimelineComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SampleSubmittedTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
