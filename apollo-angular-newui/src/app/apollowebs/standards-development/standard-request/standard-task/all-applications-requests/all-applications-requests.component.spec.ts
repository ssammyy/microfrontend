import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllApplicationsRequestsComponent } from './all-applications-requests.component';

describe('AllApplicationsRequestsComponent', () => {
  let component: AllApplicationsRequestsComponent;
  let fixture: ComponentFixture<AllApplicationsRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllApplicationsRequestsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllApplicationsRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
