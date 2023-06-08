import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewApplicationsRejectedComponent } from './review-applications-rejected.component';

describe('ReviewApplicationsRejectedComponent', () => {
  let component: ReviewApplicationsRejectedComponent;
  let fixture: ComponentFixture<ReviewApplicationsRejectedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewApplicationsRejectedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewApplicationsRejectedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
