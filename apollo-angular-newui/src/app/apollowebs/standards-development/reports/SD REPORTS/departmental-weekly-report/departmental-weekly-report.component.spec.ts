import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DepartmentalWeeklyReportComponent } from './departmental-weekly-report.component';

describe('DepartmentalWeeklyReportComponent', () => {
  let component: DepartmentalWeeklyReportComponent;
  let fixture: ComponentFixture<DepartmentalWeeklyReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DepartmentalWeeklyReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DepartmentalWeeklyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
