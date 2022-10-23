import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CorCertificatesComponent} from './cor-certificates.component';

describe('CorCertificatesComponent', () => {
  let component: CorCertificatesComponent;
  let fixture: ComponentFixture<CorCertificatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CorCertificatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CorCertificatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
