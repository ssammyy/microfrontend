import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemeMembershipReviewAssignedApplicationsComponent } from './scheme-membership-review-assigned-applications.component';

describe('SchemeMembershipReviewAssignedApplicationsComponent', () => {
  let component: SchemeMembershipReviewAssignedApplicationsComponent;
  let fixture: ComponentFixture<SchemeMembershipReviewAssignedApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchemeMembershipReviewAssignedApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchemeMembershipReviewAssignedApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
