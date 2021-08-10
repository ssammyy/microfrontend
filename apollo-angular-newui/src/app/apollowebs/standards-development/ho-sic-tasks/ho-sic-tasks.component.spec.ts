import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HoSicTasksComponent } from './ho-sic-tasks.component';

describe('HoSicTasksComponent', () => {
  let component: HoSicTasksComponent;
  let fixture: ComponentFixture<HoSicTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HoSicTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HoSicTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
