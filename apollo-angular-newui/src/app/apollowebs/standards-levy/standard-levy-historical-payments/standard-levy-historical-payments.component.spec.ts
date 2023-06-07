import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyHistoricalPaymentsComponent } from './standard-levy-historical-payments.component';

describe('StandardLevyHistoricalPaymentsComponent', () => {
  let component: StandardLevyHistoricalPaymentsComponent;
  let fixture: ComponentFixture<StandardLevyHistoricalPaymentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyHistoricalPaymentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyHistoricalPaymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
