import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdApproveChangesComponent } from './int-std-approve-changes.component';

describe('IntStdApproveChangesComponent', () => {
  let component: IntStdApproveChangesComponent;
  let fixture: ComponentFixture<IntStdApproveChangesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdApproveChangesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdApproveChangesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
