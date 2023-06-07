import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdJustificationComponent } from './int-std-justification.component';

describe('IntStdJustificationComponent', () => {
  let component: IntStdJustificationComponent;
  let fixture: ComponentFixture<IntStdJustificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdJustificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdJustificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
