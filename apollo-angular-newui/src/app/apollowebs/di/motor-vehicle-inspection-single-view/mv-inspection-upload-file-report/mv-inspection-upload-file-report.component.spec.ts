import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MvInspectionUploadFileReportComponent } from './mv-inspection-upload-file-report.component';

describe('MvInspectionUploadFileReportComponent', () => {
  let component: MvInspectionUploadFileReportComponent;
  let fixture: ComponentFixture<MvInspectionUploadFileReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MvInspectionUploadFileReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MvInspectionUploadFileReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
