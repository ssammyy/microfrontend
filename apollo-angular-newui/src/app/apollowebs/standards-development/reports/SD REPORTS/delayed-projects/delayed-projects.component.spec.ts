import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DelayedProjectsComponent } from './delayed-projects.component';

describe('DelayedProjectsComponent', () => {
  let component: DelayedProjectsComponent;
  let fixture: ComponentFixture<DelayedProjectsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DelayedProjectsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DelayedProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
