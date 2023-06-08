import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewWaiverCertificatesComponent} from './view-waiver-certificates.component';

describe('ViewWaiverCertificatesComponent', () => {
  let component: ViewWaiverCertificatesComponent;
  let fixture: ComponentFixture<ViewWaiverCertificatesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewWaiverCertificatesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewWaiverCertificatesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
