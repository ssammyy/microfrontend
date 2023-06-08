import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdLevyManufacturerPenaltyComponent } from './std-levy-manufacturer-penalty.component';

describe('StdLevyManufacturerPenaltyComponent', () => {
  let component: StdLevyManufacturerPenaltyComponent;
  let fixture: ComponentFixture<StdLevyManufacturerPenaltyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdLevyManufacturerPenaltyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdLevyManufacturerPenaltyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
