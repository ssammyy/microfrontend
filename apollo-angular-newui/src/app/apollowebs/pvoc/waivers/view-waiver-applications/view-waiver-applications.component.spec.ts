import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewWaiverApplicationsComponent } from './view-waiver-applications.component';

describe('ViewWaiverApplicationsComponent', () => {
  let component: ViewWaiverApplicationsComponent;
  let fixture: ComponentFixture<ViewWaiverApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewWaiverApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewWaiverApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
