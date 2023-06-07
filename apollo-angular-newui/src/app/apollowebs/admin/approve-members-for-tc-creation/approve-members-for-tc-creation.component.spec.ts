import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveMembersForTcCreationComponent } from './approve-members-for-tc-creation.component';

describe('ApproveMembersForTcCreationComponent', () => {
  let component: ApproveMembersForTcCreationComponent;
  let fixture: ComponentFixture<ApproveMembersForTcCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveMembersForTcCreationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveMembersForTcCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
