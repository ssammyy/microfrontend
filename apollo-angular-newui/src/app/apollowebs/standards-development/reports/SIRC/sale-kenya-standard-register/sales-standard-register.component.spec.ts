import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesStandardRegisterComponent } from './sales-standard-register.component';

describe('SalesStandardRegisterComponent', () => {
  let component: SalesStandardRegisterComponent;
  let fixture: ComponentFixture<SalesStandardRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SalesStandardRegisterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SalesStandardRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
