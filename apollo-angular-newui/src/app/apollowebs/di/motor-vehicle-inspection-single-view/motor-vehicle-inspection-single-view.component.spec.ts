import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MotorVehicleInspectionSingleViewComponent } from './motor-vehicle-inspection-single-view.component';

describe('MotorVehicleInspectionSingleViewComponent', () => {
  let component: MotorVehicleInspectionSingleViewComponent;
  let fixture: ComponentFixture<MotorVehicleInspectionSingleViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MotorVehicleInspectionSingleViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MotorVehicleInspectionSingleViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
