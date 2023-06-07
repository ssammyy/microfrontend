import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdJustificationAppComponent } from './int-std-justification-app.component';

describe('IntStdJustificationAppComponent', () => {
  let component: IntStdJustificationAppComponent;
  let fixture: ComponentFixture<IntStdJustificationAppComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdJustificationAppComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdJustificationAppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
