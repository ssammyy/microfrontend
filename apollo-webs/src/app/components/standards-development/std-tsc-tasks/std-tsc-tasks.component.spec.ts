import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdTscTasksComponent } from './std-tsc-tasks.component';

describe('StdTscTasksComponent', () => {
  let component: StdTscTasksComponent;
  let fixture: ComponentFixture<StdTscTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdTscTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdTscTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
