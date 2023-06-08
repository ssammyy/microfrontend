import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewFeedbackSacComponent } from './review-feedback-sac.component';

describe('ReviewFeedbackSPCComponent', () => {
  let component: ReviewFeedbackSacComponent;
  let fixture: ComponentFixture<ReviewFeedbackSacComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewFeedbackSacComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewFeedbackSacComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
