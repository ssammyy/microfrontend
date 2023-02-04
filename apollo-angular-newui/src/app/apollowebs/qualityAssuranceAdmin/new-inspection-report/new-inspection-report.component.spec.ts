import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewInspectionReportComponent } from './new-inspection-report.component';

describe('NewInspectionReportComponent', () => {
  let component: NewInspectionReportComponent;
  let fixture: ComponentFixture<NewInspectionReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewInspectionReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewInspectionReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
