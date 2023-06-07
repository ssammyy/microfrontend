import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoiceConsolidateFmarkComponent } from './invoice-consolidate-fmark.component';

describe('InvoiceConsolidateFmarkComponent', () => {
  let component: InvoiceConsolidateFmarkComponent;
  let fixture: ComponentFixture<InvoiceConsolidateFmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InvoiceConsolidateFmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoiceConsolidateFmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
