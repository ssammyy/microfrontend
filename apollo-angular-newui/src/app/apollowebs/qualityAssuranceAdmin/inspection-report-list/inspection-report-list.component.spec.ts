import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InspectionReportListComponent } from './inspection-report-list.component';

describe('InspectionReportListComponent', () => {
  let component: InspectionReportListComponent;
  let fixture: ComponentFixture<InspectionReportListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InspectionReportListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InspectionReportListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
