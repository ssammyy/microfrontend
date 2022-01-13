import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCorporateCustomersComponent } from './view-corporate-customers.component';

describe('ViewCorporateCustomersComponent', () => {
  let component: ViewCorporateCustomersComponent;
  let fixture: ComponentFixture<ViewCorporateCustomersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewCorporateCustomersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewCorporateCustomersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
