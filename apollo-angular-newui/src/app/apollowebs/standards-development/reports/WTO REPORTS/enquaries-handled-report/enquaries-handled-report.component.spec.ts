import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnquariesHandledReportComponent } from './enquaries-handled-report.component';

describe('EnquariesHandledReportComponent', () => {
  let component: EnquariesHandledReportComponent;
  let fixture: ComponentFixture<EnquariesHandledReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnquariesHandledReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnquariesHandledReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
