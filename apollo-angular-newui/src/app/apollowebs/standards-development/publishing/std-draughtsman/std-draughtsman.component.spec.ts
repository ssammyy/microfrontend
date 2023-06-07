import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdDraughtsmanComponent } from './std-draughtsman.component';

describe('StdDraughtsmanComponent', () => {
  let component: StdDraughtsmanComponent;
  let fixture: ComponentFixture<StdDraughtsmanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdDraughtsmanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdDraughtsmanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
