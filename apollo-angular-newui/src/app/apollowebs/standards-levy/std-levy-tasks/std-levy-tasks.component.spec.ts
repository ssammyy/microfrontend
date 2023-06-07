import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdLevyTasksComponent } from './std-levy-tasks.component';

describe('StdLevyTasksComponent', () => {
  let component: StdLevyTasksComponent;
  let fixture: ComponentFixture<StdLevyTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdLevyTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdLevyTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
