import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewFeedbackSPCComponent } from './review-feedback-spc.component';

describe('ReviewFeedbackSPCComponent', () => {
  let component: ReviewFeedbackSPCComponent;
  let fixture: ComponentFixture<ReviewFeedbackSPCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewFeedbackSPCComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewFeedbackSPCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
