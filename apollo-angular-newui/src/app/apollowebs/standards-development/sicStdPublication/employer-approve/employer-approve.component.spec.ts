import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployerApproveComponent } from './employer-approve.component';

describe('EmployerApproveComponent', () => {
  let component: EmployerApproveComponent;
  let fixture: ComponentFixture<EmployerApproveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployerApproveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployerApproveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
