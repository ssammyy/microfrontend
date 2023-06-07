import {TestBed} from '@angular/core/testing';

import {MarketSurveillanceService} from './market-surveillance.service';

describe('MarketSurveillanceService', () => {
  let service: MarketSurveillanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MarketSurveillanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
