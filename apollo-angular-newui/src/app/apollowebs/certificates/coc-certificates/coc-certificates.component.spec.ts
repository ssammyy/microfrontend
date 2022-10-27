import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CocCertificatesComponent} from './coc-certificates.component';

describe('CocCertificatesComponent', () => {
  let component: CocCertificatesComponent;
  let fixture: ComponentFixture<CocCertificatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CocCertificatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CocCertificatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
