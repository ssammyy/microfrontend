import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SacSecTasksComponent } from './sac-sec-tasks.component';

describe('SacSecTasksComponent', () => {
  let component: SacSecTasksComponent;
  let fixture: ComponentFixture<SacSecTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SacSecTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SacSecTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
