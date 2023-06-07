import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestStandardReviewComponent } from './request-standard-review.component';

describe('RequestStandardReviewComponent', () => {
  let component: RequestStandardReviewComponent;
  let fixture: ComponentFixture<RequestStandardReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestStandardReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestStandardReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
