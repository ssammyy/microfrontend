import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleInspectionChecklistComponent } from './vehicle-inspection-checklist.component';

describe('VehicleInspectionChecklistComponent', () => {
  let component: VehicleInspectionChecklistComponent;
  let fixture: ComponentFixture<VehicleInspectionChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VehicleInspectionChecklistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleInspectionChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
