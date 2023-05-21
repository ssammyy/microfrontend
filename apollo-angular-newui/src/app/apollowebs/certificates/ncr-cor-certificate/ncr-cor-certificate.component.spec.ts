import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NcrCorCertificateComponent} from './ncr-cor-certificate.component';

describe('NcrCorCertificateComponent', () => {
  let component: NcrCorCertificateComponent;
  let fixture: ComponentFixture<NcrCorCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NcrCorCertificateComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NcrCorCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
