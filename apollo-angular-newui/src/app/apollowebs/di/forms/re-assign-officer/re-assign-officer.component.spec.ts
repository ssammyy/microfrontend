import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReAssignOfficerComponent } from './re-assign-officer.component';

describe('ReAssignOfficerComponent', () => {
  let component: ReAssignOfficerComponent;
  let fixture: ComponentFixture<ReAssignOfficerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReAssignOfficerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReAssignOfficerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
