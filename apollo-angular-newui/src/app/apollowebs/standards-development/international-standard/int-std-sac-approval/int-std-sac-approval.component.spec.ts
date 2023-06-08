import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdSacApprovalComponent } from './int-std-sac-approval.component';

describe('IntStdSacApprovalComponent', () => {
  let component: IntStdSacApprovalComponent;
  let fixture: ComponentFixture<IntStdSacApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdSacApprovalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdSacApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
