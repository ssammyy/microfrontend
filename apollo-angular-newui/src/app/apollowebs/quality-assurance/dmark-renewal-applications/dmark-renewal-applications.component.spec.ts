import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkRenewalApplicationsComponent } from './dmark-renewal-applications.component';

describe('DmarkRenewalApplicationsComponent', () => {
  let component: DmarkRenewalApplicationsComponent;
  let fixture: ComponentFixture<DmarkRenewalApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkRenewalApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkRenewalApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
