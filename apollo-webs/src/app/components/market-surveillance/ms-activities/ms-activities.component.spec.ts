import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MsActivitiesComponent} from './ms-activities.component';

describe('MsActivitiesComponent', () => {
  let component: MsActivitiesComponent;
  let fixture: ComponentFixture<MsActivitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MsActivitiesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MsActivitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
