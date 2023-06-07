import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewBallotDraftComponent } from './review-ballot-draft.component';

describe('ReviewBallotDraftComponent', () => {
  let component: ReviewBallotDraftComponent;
  let fixture: ComponentFixture<ReviewBallotDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewBallotDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewBallotDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
