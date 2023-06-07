import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EngineeringInspectionChecklistComponent } from './engineering-inspection-checklist.component';

describe('EngineeringInspectionChecklistComponent', () => {
  let component: EngineeringInspectionChecklistComponent;
  let fixture: ComponentFixture<EngineeringInspectionChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EngineeringInspectionChecklistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EngineeringInspectionChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
