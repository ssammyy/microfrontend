import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentOnPublicReviewDraftComponent } from './comment-on-public-review-draft.component';

describe('CommentOnPublicReviewDraftComponent', () => {
  let component: CommentOnPublicReviewDraftComponent;
  let fixture: ComponentFixture<CommentOnPublicReviewDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommentOnPublicReviewDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentOnPublicReviewDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
