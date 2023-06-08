import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdApprovedProposalsComponent } from './int-std-approved-proposals.component';

describe('IntStdApprovedProposalsComponent', () => {
  let component: IntStdApprovedProposalsComponent;
  let fixture: ComponentFixture<IntStdApprovedProposalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdApprovedProposalsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdApprovedProposalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
