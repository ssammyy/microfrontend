import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyPenaltyReportComponent } from './standard-levy-penalty-report.component';

describe('StandardLevyPenaltyReportComponent', () => {
  let component: StandardLevyPenaltyReportComponent;
  let fixture: ComponentFixture<StandardLevyPenaltyReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyPenaltyReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyPenaltyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
