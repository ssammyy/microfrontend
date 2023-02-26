import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemeMemberShipReviewAllApplicationsComponent } from './scheme-member-ship-review-all-applications.component';

describe('SchemeMemberShipReviewAllApplicationsComponent', () => {
  let component: SchemeMemberShipReviewAllApplicationsComponent;
  let fixture: ComponentFixture<SchemeMemberShipReviewAllApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchemeMemberShipReviewAllApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchemeMemberShipReviewAllApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
