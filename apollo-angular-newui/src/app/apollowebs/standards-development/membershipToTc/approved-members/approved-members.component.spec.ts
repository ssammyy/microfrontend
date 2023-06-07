import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovedMembersComponent } from './approved-members.component';

describe('ApprovedMembersComponent', () => {
  let component: ApprovedMembersComponent;
  let fixture: ComponentFixture<ApprovedMembersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovedMembersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovedMembersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
