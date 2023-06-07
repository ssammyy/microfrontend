import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IsProposalFormComponent } from './is-proposal-form.component';

describe('IsProposalFormComponent', () => {
  let component: IsProposalFormComponent;
  let fixture: ComponentFixture<IsProposalFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IsProposalFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IsProposalFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
