import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpcReportComponent } from './spc-report.component';

describe('SpcReportComponent', () => {
  let component: SpcReportComponent;
  let fixture: ComponentFixture<SpcReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpcReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpcReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
