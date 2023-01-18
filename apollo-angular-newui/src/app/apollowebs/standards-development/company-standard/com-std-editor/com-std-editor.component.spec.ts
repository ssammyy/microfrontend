import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdEditorComponent } from './com-std-editor.component';

describe('ComStdEditorComponent', () => {
  let component: ComStdEditorComponent;
  let fixture: ComponentFixture<ComStdEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
