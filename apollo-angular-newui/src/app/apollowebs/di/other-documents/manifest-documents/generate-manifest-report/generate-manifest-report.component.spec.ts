import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenerateManifestReportComponent} from './generate-manifest-report.component';

describe('GenerateManifestReportComponent', () => {
  let component: GenerateManifestReportComponent;
  let fixture: ComponentFixture<GenerateManifestReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateManifestReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateManifestReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
