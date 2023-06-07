import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoiceConsolidateDmarkComponent } from './invoice-consolidate-dmark.component';

describe('InvoiceConsolidateDmarkComponent', () => {
  let component: InvoiceConsolidateDmarkComponent;
  let fixture: ComponentFixture<InvoiceConsolidateDmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InvoiceConsolidateDmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoiceConsolidateDmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
