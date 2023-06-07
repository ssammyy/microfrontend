import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsApprovalCommitteeComponent } from './standards-approval-committee.component';

describe('StandardsApprovalCommitteeComponent', () => {
  let component: StandardsApprovalCommitteeComponent;
  let fixture: ComponentFixture<StandardsApprovalCommitteeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsApprovalCommitteeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsApprovalCommitteeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
