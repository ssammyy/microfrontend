import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AproveRejectAuctionItemComponent } from './aprove-reject-auction-item.component';

describe('AproveRejectAuctionItemComponent', () => {
  let component: AproveRejectAuctionItemComponent;
  let fixture: ComponentFixture<AproveRejectAuctionItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AproveRejectAuctionItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AproveRejectAuctionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
