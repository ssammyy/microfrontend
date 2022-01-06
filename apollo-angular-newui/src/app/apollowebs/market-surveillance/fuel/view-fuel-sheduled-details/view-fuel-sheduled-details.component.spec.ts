import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewFuelSheduledDetailsComponent } from './view-fuel-sheduled-details.component';

describe('ViewFuelSheduledDetailsComponent', () => {
  let component: ViewFuelSheduledDetailsComponent;
  let fixture: ComponentFixture<ViewFuelSheduledDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewFuelSheduledDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewFuelSheduledDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
