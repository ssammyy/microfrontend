import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NcrCertificatesComponent} from './ncr-certificates.component';

describe('NcrCertificatesComponent', () => {
  let component: NcrCertificatesComponent;
  let fixture: ComponentFixture<NcrCertificatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NcrCertificatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NcrCertificatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
