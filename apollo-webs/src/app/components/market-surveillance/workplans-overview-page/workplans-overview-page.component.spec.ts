import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkplansOverviewPageComponent} from './workplans-overview-page.component';

describe('WorkplansOverviewPageComponent', () => {
  let component: WorkplansOverviewPageComponent;
  let fixture: ComponentFixture<WorkplansOverviewPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkplansOverviewPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkplansOverviewPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
