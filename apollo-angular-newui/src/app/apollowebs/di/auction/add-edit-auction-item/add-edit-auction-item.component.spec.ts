import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEditAuctionItemComponent } from './add-edit-auction-item.component';

describe('AddEditAuctionItemComponent', () => {
  let component: AddEditAuctionItemComponent;
  let fixture: ComponentFixture<AddEditAuctionItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddEditAuctionItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddEditAuctionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
