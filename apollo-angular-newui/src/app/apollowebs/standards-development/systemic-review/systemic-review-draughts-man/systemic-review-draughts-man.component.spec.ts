import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewDraughtsManComponent } from './systemic-review-draughts-man.component';

describe('SystemicReviewDraughtsManComponent', () => {
  let component: SystemicReviewDraughtsManComponent;
  let fixture: ComponentFixture<SystemicReviewDraughtsManComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewDraughtsManComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewDraughtsManComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
