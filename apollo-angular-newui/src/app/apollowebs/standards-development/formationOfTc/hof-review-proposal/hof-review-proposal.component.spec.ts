import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HofReviewProposalComponent } from './hof-review-proposal.component';

describe('HofReviewProposalComponent', () => {
  let component: HofReviewProposalComponent;
  let fixture: ComponentFixture<HofReviewProposalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HofReviewProposalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HofReviewProposalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
