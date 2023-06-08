import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdTscSecTasksComponentComponent } from './std-tsc-sec-tasks-component.component';

describe('StdTscSecTasksComponentComponent', () => {
  let component: StdTscSecTasksComponentComponent;
  let fixture: ComponentFixture<StdTscSecTasksComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdTscSecTasksComponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdTscSecTasksComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
