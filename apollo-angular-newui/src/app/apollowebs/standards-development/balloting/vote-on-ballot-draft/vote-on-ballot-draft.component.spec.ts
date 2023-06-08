import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VoteOnBallotDraftComponent } from './vote-on-ballot-draft.component';

describe('VoteOnBallotDraftComponent', () => {
  let component: VoteOnBallotDraftComponent;
  let fixture: ComponentFixture<VoteOnBallotDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VoteOnBallotDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VoteOnBallotDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
