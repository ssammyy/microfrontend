import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPublicReviewCommentsComponent } from './view-public-review-comments.component';

describe('ViewPublicReviewCommentsComponent', () => {
  let component: ViewPublicReviewCommentsComponent;
  let fixture: ComponentFixture<ViewPublicReviewCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewPublicReviewCommentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewPublicReviewCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
