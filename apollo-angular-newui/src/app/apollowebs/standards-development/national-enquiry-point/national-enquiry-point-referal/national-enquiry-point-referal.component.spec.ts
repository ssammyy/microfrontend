import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NationalEnquiryPointReferalComponent } from './national-enquiry-point-referal.component';

describe('NationalEnquiryPointReferalComponent', () => {
  let component: NationalEnquiryPointReferalComponent;
  let fixture: ComponentFixture<NationalEnquiryPointReferalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NationalEnquiryPointReferalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NationalEnquiryPointReferalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
