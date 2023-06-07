import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdProposalCommentsComponent } from './int-std-proposal-comments.component';

describe('IntStdProposalCommentsComponent', () => {
  let component: IntStdProposalCommentsComponent;
  let fixture: ComponentFixture<IntStdProposalCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdProposalCommentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdProposalCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
