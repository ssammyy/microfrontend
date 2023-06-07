import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewPreliminaryDraftComponent } from './review-preliminary-draft.component';

describe('ReviewPreliminaryDraftComponent', () => {
  let component: ReviewPreliminaryDraftComponent;
  let fixture: ComponentFixture<ReviewPreliminaryDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewPreliminaryDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewPreliminaryDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
