import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdHopTasksComponent } from './std-hop-tasks.component';

describe('StdHopTasksComponent', () => {
  let component: StdHopTasksComponent;
  let fixture: ComponentFixture<StdHopTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdHopTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdHopTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
