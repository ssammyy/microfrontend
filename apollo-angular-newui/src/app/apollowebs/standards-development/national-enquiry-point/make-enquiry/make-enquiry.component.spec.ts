import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakeEnquiryComponent } from './make-enquiry.component';

describe('MakeEnquiryComponent', () => {
  let component: MakeEnquiryComponent;
  let fixture: ComponentFixture<MakeEnquiryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakeEnquiryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MakeEnquiryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
