import { TestBed } from '@angular/core/testing';

import { FinanceInvoiceService } from './finance-invoice.service';

describe('FinanceInvoiceService', () => {
  let service: FinanceInvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinanceInvoiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
