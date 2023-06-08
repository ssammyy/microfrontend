import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTcMembersComponent } from './manage-tc-members.component';

describe('ManageTcMembersComponent', () => {
  let component: ManageTcMembersComponent;
  let fixture: ComponentFixture<ManageTcMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManageTcMembersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManageTcMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
