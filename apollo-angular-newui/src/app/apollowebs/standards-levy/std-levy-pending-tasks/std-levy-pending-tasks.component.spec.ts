import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdLevyPendingTasksComponent } from './std-levy-pending-tasks.component';

describe('StdLevyPendingTasksComponent', () => {
  let component: StdLevyPendingTasksComponent;
  let fixture: ComponentFixture<StdLevyPendingTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdLevyPendingTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdLevyPendingTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
