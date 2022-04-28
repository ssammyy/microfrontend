import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateAuctionKraReportComponent } from './generate-auction-kra-report.component';

describe('GenerateAuctionKraReportComponent', () => {
  let component: GenerateAuctionKraReportComponent;
  let fixture: ComponentFixture<GenerateAuctionKraReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateAuctionKraReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateAuctionKraReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
