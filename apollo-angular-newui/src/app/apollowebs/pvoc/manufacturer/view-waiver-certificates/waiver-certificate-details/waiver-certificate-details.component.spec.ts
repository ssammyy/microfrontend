import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WaiverCertificateDetailsComponent} from './waiver-certificate-details.component';

describe('WaiverCertificateDetailsComponent', () => {
  let component: WaiverCertificateDetailsComponent;
  let fixture: ComponentFixture<WaiverCertificateDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WaiverCertificateDetailsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WaiverCertificateDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
