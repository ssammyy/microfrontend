import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignOfficerComponent } from './assign-officer.component';

describe('AssignOfficerComponent', () => {
  let component: AssignOfficerComponent;
  let fixture: ComponentFixture<AssignOfficerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssignOfficerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignOfficerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
