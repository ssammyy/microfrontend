import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicReviewDraftComponent } from './public-review-draft.component';

describe('PublicReviewDraftComponent', () => {
  let component: PublicReviewDraftComponent;
  let fixture: ComponentFixture<PublicReviewDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PublicReviewDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublicReviewDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
