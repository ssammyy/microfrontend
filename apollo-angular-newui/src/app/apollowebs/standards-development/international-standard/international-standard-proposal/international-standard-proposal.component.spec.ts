import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternationalStandardProposalComponent } from './international-standard-proposal.component';

describe('InternationalStandardProposalComponent', () => {
  let component: InternationalStandardProposalComponent;
  let fixture: ComponentFixture<InternationalStandardProposalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InternationalStandardProposalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InternationalStandardProposalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
