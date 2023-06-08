import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembersToCreateCredentialsComponent } from './members-to-create-credentials.component';

describe('MembersToCreateCredentialsComponent', () => {
  let component: MembersToCreateCredentialsComponent;
  let fixture: ComponentFixture<MembersToCreateCredentialsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MembersToCreateCredentialsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MembersToCreateCredentialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
