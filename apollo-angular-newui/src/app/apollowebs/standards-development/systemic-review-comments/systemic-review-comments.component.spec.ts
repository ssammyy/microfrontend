import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewCommentsComponent } from './systemic-review-comments.component';

describe('SystemicReviewCommentsComponent', () => {
  let component: SystemicReviewCommentsComponent;
  let fixture: ComponentFixture<SystemicReviewCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewCommentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
