import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GeneralCertificatesComponent} from './general-certificates.component';

describe('GeneralCertificatesComponent', () => {
  let component: GeneralCertificatesComponent;
  let fixture: ComponentFixture<GeneralCertificatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GeneralCertificatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GeneralCertificatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
