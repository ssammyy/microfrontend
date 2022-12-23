import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FieldInspectionSummaryComponent } from './field-inspection-summary.component';

describe('FieldInspectionSummaryComponent', () => {
  let component: FieldInspectionSummaryComponent;
  let fixture: ComponentFixture<FieldInspectionSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FieldInspectionSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FieldInspectionSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
