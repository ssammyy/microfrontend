import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdNscApprovalComponent } from './int-std-nsc-approval.component';

describe('IntStdNscApprovalComponent', () => {
  let component: IntStdNscApprovalComponent;
  let fixture: ComponentFixture<IntStdNscApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdNscApprovalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdNscApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
