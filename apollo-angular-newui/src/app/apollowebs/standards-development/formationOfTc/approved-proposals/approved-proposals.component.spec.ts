import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovedProposalsComponent } from './approved-proposals.component';

describe('ApprovedProposalsComponent', () => {
  let component: ApprovedProposalsComponent;
  let fixture: ComponentFixture<ApprovedProposalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovedProposalsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovedProposalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
