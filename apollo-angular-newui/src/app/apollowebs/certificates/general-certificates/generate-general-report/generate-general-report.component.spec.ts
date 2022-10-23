import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenerateGeneralReportComponent} from './generate-general-report.component';

describe('GenerateGeneralReportComponent', () => {
  let component: GenerateGeneralReportComponent;
  let fixture: ComponentFixture<GenerateGeneralReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateGeneralReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateGeneralReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
