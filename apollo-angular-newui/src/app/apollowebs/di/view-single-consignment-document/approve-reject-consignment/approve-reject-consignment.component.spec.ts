import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveRejectConsignmentComponent } from './approve-reject-consignment.component';

describe('ApproveRejectConsignmentComponent', () => {
  let component: ApproveRejectConsignmentComponent;
  let fixture: ComponentFixture<ApproveRejectConsignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveRejectConsignmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveRejectConsignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
