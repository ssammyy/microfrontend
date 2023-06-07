import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewExemptionCertificatesComponent} from './view-exemption-certificates.component';

describe('ViewExemptionCertificatesComponent', () => {
  let component: ViewExemptionCertificatesComponent;
  let fixture: ComponentFixture<ViewExemptionCertificatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewExemptionCertificatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewExemptionCertificatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
