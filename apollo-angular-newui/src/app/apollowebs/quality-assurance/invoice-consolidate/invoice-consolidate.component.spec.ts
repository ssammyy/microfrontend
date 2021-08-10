import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvoiceConsolidateComponent } from './invoice-consolidate.component';

describe('InvoiceConsolidateComponent', () => {
  let component: InvoiceConsolidateComponent;
  let fixture: ComponentFixture<InvoiceConsolidateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InvoiceConsolidateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoiceConsolidateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
