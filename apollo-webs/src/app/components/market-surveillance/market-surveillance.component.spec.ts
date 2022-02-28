import {ComponentFixture, TestBed} from '@angular/core/testing';

import {MarketSurveillanceComponent} from './market-surveillance.component';

describe('MarketSurveillanceComponent', () => {
  let component: MarketSurveillanceComponent;
  let fixture: ComponentFixture<MarketSurveillanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MarketSurveillanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketSurveillanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
