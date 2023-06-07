import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewJustificationOfTCComponent } from './review-justification-of-tc.component';

describe('ReviewJustificationOfTCComponent', () => {
  let component: ReviewJustificationOfTCComponent;
  let fixture: ComponentFixture<ReviewJustificationOfTCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewJustificationOfTCComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewJustificationOfTCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
