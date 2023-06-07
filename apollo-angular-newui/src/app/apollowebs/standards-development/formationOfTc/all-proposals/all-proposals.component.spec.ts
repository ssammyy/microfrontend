import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllProposalsComponent } from './all-proposals.component';

describe('AllProposalsComponent', () => {
  let component: AllProposalsComponent;
  let fixture: ComponentFixture<AllProposalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllProposalsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllProposalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
