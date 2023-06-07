import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PscTasksComponent } from './psc-tasks.component';

describe('PscTasksComponent', () => {
  let component: PscTasksComponent;
  let fixture: ComponentFixture<PscTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PscTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PscTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
