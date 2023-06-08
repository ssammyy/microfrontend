import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewRecommendationOfSpcComponentComponent } from './review-recommendation-of-spc-component.component';

describe('ReviewRecommendationOfSpcComponentComponent', () => {
  let component: ReviewRecommendationOfSpcComponentComponent;
  let fixture: ComponentFixture<ReviewRecommendationOfSpcComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewRecommendationOfSpcComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewRecommendationOfSpcComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
