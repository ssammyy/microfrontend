import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InspectionReport } from './inspection-report';

describe('InspectionReportInspectionTesingComponent', () => {
  let component: InspectionReport;
  let fixture: ComponentFixture<InspectionReport>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InspectionReport ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InspectionReport);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
