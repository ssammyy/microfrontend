import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdTcTasksComponent } from './std-tc-tasks.component';

describe('StdTcTasksComponent', () => {
  let component: StdTcTasksComponent;
  let fixture: ComponentFixture<StdTcTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdTcTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdTcTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
