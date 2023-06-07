import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentOnPublicReviewComponent } from './comment-on-public-review.component';

describe('CommentOnPublicReviewComponent', () => {
  let component: CommentOnPublicReviewComponent;
  let fixture: ComponentFixture<CommentOnPublicReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommentOnPublicReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentOnPublicReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
