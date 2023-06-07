import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewExemptionApplicationsComponent } from './view-exemption-applications.component';

describe('ViewExemptionApplicationsComponent', () => {
  let component: ViewExemptionApplicationsComponent;
  let fixture: ComponentFixture<ViewExemptionApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewExemptionApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewExemptionApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
