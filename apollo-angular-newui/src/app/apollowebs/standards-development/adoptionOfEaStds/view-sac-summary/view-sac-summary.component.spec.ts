import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSacSummaryComponent } from './view-sac-summary.component';

describe('ViewSacSummaryComponent', () => {
  let component: ViewSacSummaryComponent;
  let fixture: ComponentFixture<ViewSacSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewSacSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewSacSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
