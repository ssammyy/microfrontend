import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveCommitteeDraftComponent } from './approve-committee-draft.component';

describe('ApproveCommitteeDraftComponent', () => {
  let component: ApproveCommitteeDraftComponent;
  let fixture: ComponentFixture<ApproveCommitteeDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveCommitteeDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveCommitteeDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
