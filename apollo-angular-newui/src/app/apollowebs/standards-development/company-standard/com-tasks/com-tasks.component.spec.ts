import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComTasksComponent } from './com-tasks.component';

describe('ComTasksComponent', () => {
  let component: ComTasksComponent;
  let fixture: ComponentFixture<ComTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
