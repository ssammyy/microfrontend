import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinistryInspectionHomeComponent } from './ministry-inspection-home.component';

describe('MinistryInspectionHomeComponent', () => {
  let component: MinistryInspectionHomeComponent;
  let fixture: ComponentFixture<MinistryInspectionHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MinistryInspectionHomeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MinistryInspectionHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
