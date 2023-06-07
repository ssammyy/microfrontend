import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewApplicationRejectedByNscComponent } from './review-application-rejected-by-nsc.component';

describe('ReviewApplicationRejectedByNscComponent', () => {
  let component: ReviewApplicationRejectedByNscComponent;
  let fixture: ComponentFixture<ReviewApplicationRejectedByNscComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewApplicationRejectedByNscComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewApplicationRejectedByNscComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
