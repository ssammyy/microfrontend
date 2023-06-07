import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewInspectionReportComponent } from './view-inspection-report.component';

describe('NewInspectionReportComponent', () => {
  let component: ViewInspectionReportComponent;
  let fixture: ComponentFixture<ViewInspectionReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewInspectionReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewInspectionReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
