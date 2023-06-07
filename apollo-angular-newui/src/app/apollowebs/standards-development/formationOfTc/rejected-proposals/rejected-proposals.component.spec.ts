import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectedProposalsComponent } from './rejected-proposals.component';

describe('RejectedProposalsComponent', () => {
  let component: RejectedProposalsComponent;
  let fixture: ComponentFixture<RejectedProposalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectedProposalsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RejectedProposalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
