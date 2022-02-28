import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MsActivitiesDeskTasksComponent} from './ms-activities-desk-tasks.component';

describe('MsActivitiesDeskTasksComponent', () => {
  let component: MsActivitiesDeskTasksComponent;
  let fixture: ComponentFixture<MsActivitiesDeskTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MsActivitiesDeskTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MsActivitiesDeskTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
