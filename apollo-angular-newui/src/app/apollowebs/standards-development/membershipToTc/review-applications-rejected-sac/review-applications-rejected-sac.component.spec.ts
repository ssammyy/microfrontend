import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewApplicationsRejectedSacComponent } from './review-applications-rejected-sac.component';

describe('ReviewApplicationsRejectedSacComponent', () => {
  let component: ReviewApplicationsRejectedSacComponent;
  let fixture: ComponentFixture<ReviewApplicationsRejectedSacComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewApplicationsRejectedSacComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewApplicationsRejectedSacComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
