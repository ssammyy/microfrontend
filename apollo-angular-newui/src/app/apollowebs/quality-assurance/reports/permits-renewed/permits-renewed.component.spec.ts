import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermitsRenewedComponent } from './permits-renewed.component';

describe('PermitsRenewedComponent', () => {
  let component: PermitsRenewedComponent;
  let fixture: ComponentFixture<PermitsRenewedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PermitsRenewedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PermitsRenewedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
