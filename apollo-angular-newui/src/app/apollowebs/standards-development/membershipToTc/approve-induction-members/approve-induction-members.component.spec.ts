import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveInductionMembersComponent } from './approve-induction-members.component';

describe('ApproveInductionMembersComponent', () => {
  let component: ApproveInductionMembersComponent;
  let fixture: ComponentFixture<ApproveInductionMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveInductionMembersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveInductionMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
