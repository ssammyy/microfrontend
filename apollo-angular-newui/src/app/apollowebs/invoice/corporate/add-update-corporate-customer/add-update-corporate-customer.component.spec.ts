import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUpdateCorporateCustomerComponent } from './add-update-corporate-customer.component';

describe('AddUpdateCorporateCustomerComponent', () => {
  let component: AddUpdateCorporateCustomerComponent;
  let fixture: ComponentFixture<AddUpdateCorporateCustomerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddUpdateCorporateCustomerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUpdateCorporateCustomerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
