import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChecklistDataFormComponent } from './checklist-data-form.component';

describe('ChecklistDataFormComponent', () => {
  let component: ChecklistDataFormComponent;
  let fixture: ComponentFixture<ChecklistDataFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChecklistDataFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChecklistDataFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
