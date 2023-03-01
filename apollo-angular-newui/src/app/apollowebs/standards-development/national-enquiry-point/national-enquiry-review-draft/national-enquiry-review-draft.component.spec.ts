import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NationalEnquiryReviewDraftComponent } from './national-enquiry-review-draft.component';

describe('NationalEnquiryReviewDraftComponent', () => {
  let component: NationalEnquiryReviewDraftComponent;
  let fixture: ComponentFixture<NationalEnquiryReviewDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NationalEnquiryReviewDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NationalEnquiryReviewDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
