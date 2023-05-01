import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdWebProposalComponent } from './int-std-web-proposal.component';

describe('IntStdWebProposalComponent', () => {
  let component: IntStdWebProposalComponent;
  let fixture: ComponentFixture<IntStdWebProposalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdWebProposalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdWebProposalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
