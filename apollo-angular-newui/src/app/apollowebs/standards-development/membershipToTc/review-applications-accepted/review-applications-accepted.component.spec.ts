import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewApplicationsAcceptedComponent } from './review-applications-accepted.component';

describe('ReviewApplicationsAcceptedComponent', () => {
  let component: ReviewApplicationsAcceptedComponent;
  let fixture: ComponentFixture<ReviewApplicationsAcceptedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewApplicationsAcceptedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewApplicationsAcceptedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
