import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemDetailsListViewComponent } from './item-details-list-view.component';

describe('ItemDetailsListViewComponent', () => {
  let component: ItemDetailsListViewComponent;
  let fixture: ComponentFixture<ItemDetailsListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ItemDetailsListViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemDetailsListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
