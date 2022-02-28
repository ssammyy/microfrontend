import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MsActivitiesFieldTasksComponent} from './ms-activities-field-tasks.component';

describe('MsActivitiesFieldTasksComponent', () => {
  let component: MsActivitiesFieldTasksComponent;
  let fixture: ComponentFixture<MsActivitiesFieldTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MsActivitiesFieldTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MsActivitiesFieldTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
