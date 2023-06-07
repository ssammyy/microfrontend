import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndividualWeeklyReportComponent } from './individual-weekly-report.component';

describe('IndividualWeeklyReportComponent', () => {
  let component: IndividualWeeklyReportComponent;
  let fixture: ComponentFixture<IndividualWeeklyReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IndividualWeeklyReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IndividualWeeklyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
