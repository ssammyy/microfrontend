import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewComplaintDetailsComponent } from './view-complaint-details.component';

describe('ViewComplaintDetailsComponent', () => {
  let component: ViewComplaintDetailsComponent;
  let fixture: ComponentFixture<ViewComplaintDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewComplaintDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewComplaintDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
