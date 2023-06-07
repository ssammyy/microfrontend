import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkRenewalApplicationsComponent } from './smark-renewal-applications.component';

describe('SmarkRenewalApplicationsComponent', () => {
  let component: SmarkRenewalApplicationsComponent;
  let fixture: ComponentFixture<SmarkRenewalApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkRenewalApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkRenewalApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
