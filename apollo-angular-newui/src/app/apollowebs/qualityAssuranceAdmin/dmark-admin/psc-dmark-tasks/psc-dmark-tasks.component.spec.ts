import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PscDmarkTasksComponent } from './psc-dmark-tasks.component';

describe('PscDmarkTasksComponent', () => {
  let component: PscDmarkTasksComponent;
  let fixture: ComponentFixture<PscDmarkTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PscDmarkTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PscDmarkTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
