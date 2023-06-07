import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditorTasksComponent } from './editor-tasks.component';

describe('EditorTasksComponent', () => {
  let component: EditorTasksComponent;
  let fixture: ComponentFixture<EditorTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditorTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditorTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
