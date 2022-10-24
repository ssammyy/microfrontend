import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CoiCertificatesComponent} from './coi-certificates.component';

describe('CoiCertificatesComponent', () => {
  let component: CoiCertificatesComponent;
  let fixture: ComponentFixture<CoiCertificatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CoiCertificatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CoiCertificatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
