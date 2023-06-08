import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdProposalsComponent } from './int-std-proposals.component';

describe('IntStdProposalsComponent', () => {
  let component: IntStdProposalsComponent;
  let fixture: ComponentFixture<IntStdProposalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdProposalsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdProposalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
