import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewRemarksHistoryComponent } from './view-remarks-history.component';

describe('ViewRemarksHistoryComponent', () => {
  let component: ViewRemarksHistoryComponent;
  let fixture: ComponentFixture<ViewRemarksHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewRemarksHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewRemarksHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
