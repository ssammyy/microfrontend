import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManualAssignOfficerComponent } from './manual-assign-officer.component';

describe('ManualAssignOfficerComponent', () => {
  let component: ManualAssignOfficerComponent;
  let fixture: ComponentFixture<ManualAssignOfficerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManualAssignOfficerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManualAssignOfficerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
