import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TcMemberAllVotesComponent } from './tc-member-all-votes.component';

describe('TcMemberAllVotesComponent', () => {
  let component: TcMemberAllVotesComponent;
  let fixture: ComponentFixture<TcMemberAllVotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TcMemberAllVotesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TcMemberAllVotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
