import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainProductionMachineryComponent } from './main-production-machinery.component';

describe('MainProductionMachineryComponent', () => {
  let component: MainProductionMachineryComponent;
  let fixture: ComponentFixture<MainProductionMachineryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MainProductionMachineryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MainProductionMachineryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
