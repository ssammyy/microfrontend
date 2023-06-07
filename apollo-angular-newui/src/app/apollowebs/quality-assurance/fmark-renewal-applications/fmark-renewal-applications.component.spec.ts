import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkRenewalApplicationsComponent } from './fmark-renewal-applications.component';

describe('FmarkRenewalApplicationsComponent', () => {
  let component: FmarkRenewalApplicationsComponent;
  let fixture: ComponentFixture<FmarkRenewalApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkRenewalApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkRenewalApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
