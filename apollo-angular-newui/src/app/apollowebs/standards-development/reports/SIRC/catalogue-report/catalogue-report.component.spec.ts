import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CatalogueReportComponent } from './catalogue-report.component';

describe('CatalogueReportComponent', () => {
  let component: CatalogueReportComponent;
  let fixture: ComponentFixture<CatalogueReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CatalogueReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CatalogueReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
