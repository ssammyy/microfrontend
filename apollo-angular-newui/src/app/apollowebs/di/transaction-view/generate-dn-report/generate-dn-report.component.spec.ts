import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenerateDnReportComponent} from './generate-dn-report.component';

describe('GenerateDnReportComponent', () => {
  let component: GenerateDnReportComponent;
  let fixture: ComponentFixture<GenerateDnReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GenerateDnReportComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateDnReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
