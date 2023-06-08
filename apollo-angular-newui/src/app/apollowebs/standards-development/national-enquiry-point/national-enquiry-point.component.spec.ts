import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NationalEnquiryPointComponent } from './national-enquiry-point.component';

describe('NationalEnquiryPointComponent', () => {
  let component: NationalEnquiryPointComponent;
  let fixture: ComponentFixture<NationalEnquiryPointComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NationalEnquiryPointComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NationalEnquiryPointComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
