import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllVotesByOtherTcMembersComponent } from './all-votes-by-other-tc-members.component';

describe('AllVotesByOtherTcMembersComponent', () => {
  let component: AllVotesByOtherTcMembersComponent;
  let fixture: ComponentFixture<AllVotesByOtherTcMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllVotesByOtherTcMembersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllVotesByOtherTcMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
