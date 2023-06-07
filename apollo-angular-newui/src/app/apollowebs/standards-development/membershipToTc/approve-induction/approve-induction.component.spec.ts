import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveInductionComponent } from './approve-induction.component';

describe('ApproveInductionComponent', () => {
  let component: ApproveInductionComponent;
  let fixture: ComponentFixture<ApproveInductionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveInductionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveInductionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
