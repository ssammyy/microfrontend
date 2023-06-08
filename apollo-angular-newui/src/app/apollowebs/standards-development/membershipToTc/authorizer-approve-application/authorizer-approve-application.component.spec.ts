import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorizerApproveApplicationComponent } from './authorizer-approve-application.component';

describe('AuthorizerApproveApplicationComponent', () => {
  let component: AuthorizerApproveApplicationComponent;
  let fixture: ComponentFixture<AuthorizerApproveApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuthorizerApproveApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorizerApproveApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
