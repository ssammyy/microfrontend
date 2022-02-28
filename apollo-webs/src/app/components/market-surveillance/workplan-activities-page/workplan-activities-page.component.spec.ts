import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkplanActivitiesPageComponent} from './workplan-activities-page.component';

describe('WorkplanActivitiesPageComponent', () => {
  let component: WorkplanActivitiesPageComponent;
  let fixture: ComponentFixture<WorkplanActivitiesPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkplanActivitiesPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkplanActivitiesPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
