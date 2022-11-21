import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GenerateDeclarationReportComponent} from './generate-declaration-report.component';

describe('GenerateDeclarationReportComponent', () => {
  let component: GenerateDeclarationReportComponent;
  let fixture: ComponentFixture<GenerateDeclarationReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GenerateDeclarationReportComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateDeclarationReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
