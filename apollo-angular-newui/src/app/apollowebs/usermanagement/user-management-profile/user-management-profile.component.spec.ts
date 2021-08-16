import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserManagementProfileComponent } from './user-management-profile.component';

describe('UserManagementProfileComponent', () => {
  let component: UserManagementProfileComponent;
  let fixture: ComponentFixture<UserManagementProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserManagementProfileComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserManagementProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
