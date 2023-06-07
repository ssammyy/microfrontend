import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NationalEnquiryPointResponseComponent } from './national-enquiry-point-response.component';

describe('NationalEnquiryPointResponseComponent', () => {
  let component: NationalEnquiryPointResponseComponent;
  let fixture: ComponentFixture<NationalEnquiryPointResponseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NationalEnquiryPointResponseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NationalEnquiryPointResponseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
