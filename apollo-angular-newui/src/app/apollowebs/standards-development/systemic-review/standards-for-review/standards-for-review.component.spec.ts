import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsForReviewComponent } from './standards-for-review.component';

describe('StandardsForReviewComponent', () => {
  let component: StandardsForReviewComponent;
  let fixture: ComponentFixture<StandardsForReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsForReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsForReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
