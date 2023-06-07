import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenerateCDReportComponent} from './generate-cdreport.component';

describe('GenerateCDReportComponent', () => {
  let component: GenerateCDReportComponent;
  let fixture: ComponentFixture<GenerateCDReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateCDReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateCDReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
