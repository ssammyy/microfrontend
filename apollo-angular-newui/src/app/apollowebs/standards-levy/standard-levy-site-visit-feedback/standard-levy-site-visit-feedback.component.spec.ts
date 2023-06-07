import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevySiteVisitFeedbackComponent } from './standard-levy-site-visit-feedback.component';

describe('StandardLevySiteVisitFeedbackComponent', () => {
  let component: StandardLevySiteVisitFeedbackComponent;
  let fixture: ComponentFixture<StandardLevySiteVisitFeedbackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevySiteVisitFeedbackComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevySiteVisitFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
