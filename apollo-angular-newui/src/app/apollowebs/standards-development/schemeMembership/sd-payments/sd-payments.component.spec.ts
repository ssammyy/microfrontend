import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SdPaymentsComponent } from './sd-payments.component';

describe('SdPaymentsComponent', () => {
  let component: SdPaymentsComponent;
  let fixture: ComponentFixture<SdPaymentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SdPaymentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SdPaymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
