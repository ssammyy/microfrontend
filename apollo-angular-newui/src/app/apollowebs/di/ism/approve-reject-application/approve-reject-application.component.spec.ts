import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveRejectApplicationComponent } from './approve-reject-application.component';

describe('ApproveRejectApplicationComponent', () => {
  let component: ApproveRejectApplicationComponent;
  let fixture: ComponentFixture<ApproveRejectApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveRejectApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveRejectApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
