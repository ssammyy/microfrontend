import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdEditorComponent } from './int-std-editor.component';

describe('IntStdEditorComponent', () => {
  let component: IntStdEditorComponent;
  let fixture: ComponentFixture<IntStdEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
