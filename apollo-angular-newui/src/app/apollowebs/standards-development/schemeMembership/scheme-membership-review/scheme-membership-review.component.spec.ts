import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemeMembershipReviewComponent } from './scheme-membership-review.component';

describe('SchemeMembershipReviewComponent', () => {
  let component: SchemeMembershipReviewComponent;
  let fixture: ComponentFixture<SchemeMembershipReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchemeMembershipReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchemeMembershipReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
