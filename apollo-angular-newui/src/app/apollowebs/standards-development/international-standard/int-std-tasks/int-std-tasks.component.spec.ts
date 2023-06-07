import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdTasksComponent } from './int-std-tasks.component';

describe('IntStdTasksComponent', () => {
  let component: IntStdTasksComponent;
  let fixture: ComponentFixture<IntStdTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
