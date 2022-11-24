import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdCheckRequirementsComponent } from './int-std-check-requirements.component';

describe('IntStdCheckRequirementsComponent', () => {
  let component: IntStdCheckRequirementsComponent;
  let fixture: ComponentFixture<IntStdCheckRequirementsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdCheckRequirementsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdCheckRequirementsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
