import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SicpaymentconfirmComponent } from './sicpaymentconfirm.component';

describe('SicpaymentconfirmComponent', () => {
  let component: SicpaymentconfirmComponent;
  let fixture: ComponentFixture<SicpaymentconfirmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SicpaymentconfirmComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SicpaymentconfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
