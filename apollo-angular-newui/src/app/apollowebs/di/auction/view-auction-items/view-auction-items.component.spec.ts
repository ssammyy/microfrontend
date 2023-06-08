import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewAuctionItemsComponent } from './view-auction-items.component';

describe('ViewAuctionItemsComponent', () => {
  let component: ViewAuctionItemsComponent;
  let fixture: ComponentFixture<ViewAuctionItemsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewAuctionItemsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewAuctionItemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
