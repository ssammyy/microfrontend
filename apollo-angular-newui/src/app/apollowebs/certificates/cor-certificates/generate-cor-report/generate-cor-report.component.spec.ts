import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenerateCorReportComponent} from './generate-cor-report.component';

describe('GenerateCorReportComponent', () => {
  let component: GenerateCorReportComponent;
  let fixture: ComponentFixture<GenerateCorReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateCorReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateCorReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
