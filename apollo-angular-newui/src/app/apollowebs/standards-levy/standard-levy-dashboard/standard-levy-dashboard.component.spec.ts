import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyDashboardComponent } from './standard-levy-dashboard.component';

describe('StandardLevyDashboardComponent', () => {
  let component: StandardLevyDashboardComponent;
  let fixture: ComponentFixture<StandardLevyDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyDashboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
