import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherInspectionChecklistComponent } from './other-inspection-checklist.component';

describe('OtherInspectionChecklistComponent', () => {
  let component: OtherInspectionChecklistComponent;
  let fixture: ComponentFixture<OtherInspectionChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OtherInspectionChecklistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OtherInspectionChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
