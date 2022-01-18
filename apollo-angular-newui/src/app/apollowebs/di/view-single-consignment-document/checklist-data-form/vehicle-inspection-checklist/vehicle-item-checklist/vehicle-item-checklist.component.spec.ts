import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleItemChecklistComponent } from './vehicle-item-checklist.component';

describe('VehicleItemChecklistComponent', () => {
  let component: VehicleItemChecklistComponent;
  let fixture: ComponentFixture<VehicleItemChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VehicleItemChecklistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleItemChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
