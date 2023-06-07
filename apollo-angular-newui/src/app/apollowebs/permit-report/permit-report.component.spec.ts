import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermitReportComponent } from './permit-report.component';

describe('PermitReportComponent', () => {
  let component: PermitReportComponent;
  let fixture: ComponentFixture<PermitReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PermitReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PermitReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
