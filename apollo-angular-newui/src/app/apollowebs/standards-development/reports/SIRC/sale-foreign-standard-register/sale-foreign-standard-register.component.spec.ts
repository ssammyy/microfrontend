import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaleForeignStandardRegisterComponent } from './sale-foreign-standard-register.component';

describe('SaleForeignStandardRegisterComponent', () => {
  let component: SaleForeignStandardRegisterComponent;
  let fixture: ComponentFixture<SaleForeignStandardRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SaleForeignStandardRegisterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SaleForeignStandardRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
