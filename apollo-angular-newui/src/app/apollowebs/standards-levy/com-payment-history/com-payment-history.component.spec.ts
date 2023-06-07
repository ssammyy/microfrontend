import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComPaymentHistoryComponent } from './com-payment-history.component';

describe('ComPaymentHistoryComponent', () => {
  let component: ComPaymentHistoryComponent;
  let fixture: ComponentFixture<ComPaymentHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComPaymentHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComPaymentHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
