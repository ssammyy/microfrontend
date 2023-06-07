import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewCommitteeDraftComponent } from './review-committee-draft.component';

describe('ReviewCommitteeDraftComponent', () => {
  let component: ReviewCommitteeDraftComponent;
  let fixture: ComponentFixture<ReviewCommitteeDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewCommitteeDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewCommitteeDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
