import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembersCreatedCredentialsComponent } from './members-created-credentials.component';

describe('MembersCreatedCredentialsComponent', () => {
  let component: MembersCreatedCredentialsComponent;
  let fixture: ComponentFixture<MembersCreatedCredentialsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MembersCreatedCredentialsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MembersCreatedCredentialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
