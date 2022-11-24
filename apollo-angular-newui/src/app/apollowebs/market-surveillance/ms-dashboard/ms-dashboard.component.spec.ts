import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MsDashboardComponent } from './ms-dashboard.component';

describe('MsDashboardComponent', () => {
  let component: MsDashboardComponent;
  let fixture: ComponentFixture<MsDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MsDashboardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MsDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
