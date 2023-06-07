import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewBillLimitsComponent } from './view-bill-limits.component';

describe('ViewBillLimitsComponent', () => {
  let component: ViewBillLimitsComponent;
  let fixture: ComponentFixture<ViewBillLimitsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewBillLimitsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewBillLimitsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
