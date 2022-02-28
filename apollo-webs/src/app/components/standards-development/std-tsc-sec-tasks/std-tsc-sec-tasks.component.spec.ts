import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdTscSecTasksComponent } from './std-tsc-sec-tasks.component';

describe('StdTscSecTasksComponent', () => {
  let component: StdTscSecTasksComponent;
  let fixture: ComponentFixture<StdTscSecTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdTscSecTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdTscSecTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
