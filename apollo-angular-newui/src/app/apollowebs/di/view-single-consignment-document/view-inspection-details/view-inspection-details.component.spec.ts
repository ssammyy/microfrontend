import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewInspectionDetailsComponent } from './view-inspection-details.component';

describe('ViewInspectionDetailsComponent', () => {
  let component: ViewInspectionDetailsComponent;
  let fixture: ComponentFixture<ViewInspectionDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewInspectionDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewInspectionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
