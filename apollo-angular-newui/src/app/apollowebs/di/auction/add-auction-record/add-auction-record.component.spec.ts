import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAuctionRecordComponent } from './add-auction-record.component';

describe('AddAuctionRecordComponent', () => {
  let component: AddAuctionRecordComponent;
  let fixture: ComponentFixture<AddAuctionRecordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddAuctionRecordComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddAuctionRecordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
