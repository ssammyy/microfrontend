import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AdminBusinessManagementComponent} from './admin-business-management.component';

describe('AdminBusinessManagementComponent', () => {
  let component: AdminBusinessManagementComponent;
  let fixture: ComponentFixture<AdminBusinessManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminBusinessManagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminBusinessManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
