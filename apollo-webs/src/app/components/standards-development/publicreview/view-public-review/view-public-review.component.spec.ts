import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewPublicReviewComponent } from './view-public-review.component';

describe('ViewPublicReviewComponent', () => {
  let component: ViewPublicReviewComponent;
  let fixture: ComponentFixture<ViewPublicReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewPublicReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewPublicReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
