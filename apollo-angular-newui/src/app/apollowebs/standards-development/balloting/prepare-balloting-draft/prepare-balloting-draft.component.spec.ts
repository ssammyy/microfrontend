import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrepareBallotingDraftComponent } from './prepare-balloting-draft.component';

describe('PrepareBallotingDraftComponent', () => {
  let component: PrepareBallotingDraftComponent;
  let fixture: ComponentFixture<PrepareBallotingDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrepareBallotingDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrepareBallotingDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
