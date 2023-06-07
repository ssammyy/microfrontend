import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DraughsmanReportComponent } from './draughsman-report.component';

describe('DraughsmanReportComponent', () => {
  let component: DraughsmanReportComponent;
  let fixture: ComponentFixture<DraughsmanReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DraughsmanReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DraughsmanReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
