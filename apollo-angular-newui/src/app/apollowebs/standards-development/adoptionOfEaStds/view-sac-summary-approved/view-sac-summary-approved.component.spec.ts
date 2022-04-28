import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSacSummaryApprovedComponent } from './view-sac-summary-approved.component';

describe('ViewSacSummaryApprovedComponent', () => {
  let component: ViewSacSummaryApprovedComponent;
  let fixture: ComponentFixture<ViewSacSummaryApprovedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewSacSummaryApprovedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewSacSummaryApprovedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
