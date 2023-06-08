import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssignAuctionItemComponent } from './assign-auction-item.component';

describe('AssignAuctionItemComponent', () => {
  let component: AssignAuctionItemComponent;
  let fixture: ComponentFixture<AssignAuctionItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AssignAuctionItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignAuctionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
