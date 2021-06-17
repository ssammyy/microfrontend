import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewLoginSignupComponent } from './new-login-signup.component';

describe('NewLoginSignupComponent', () => {
  let component: NewLoginSignupComponent;
  let fixture: ComponentFixture<NewLoginSignupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewLoginSignupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewLoginSignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
