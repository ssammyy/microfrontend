import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreparePublicReviewDraftComponent } from './prepare-public-review-draft.component';

describe('PreparePublicReviewDraftComponent', () => {
  let component: PreparePublicReviewDraftComponent;
  let fixture: ComponentFixture<PreparePublicReviewDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreparePublicReviewDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreparePublicReviewDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
