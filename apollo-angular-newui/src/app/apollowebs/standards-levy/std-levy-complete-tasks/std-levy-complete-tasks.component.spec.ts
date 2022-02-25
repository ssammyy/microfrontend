import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdLevyCompleteTasksComponent } from './std-levy-complete-tasks.component';

describe('StdLevyCompleteTasksComponent', () => {
  let component: StdLevyCompleteTasksComponent;
  let fixture: ComponentFixture<StdLevyCompleteTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdLevyCompleteTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdLevyCompleteTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
