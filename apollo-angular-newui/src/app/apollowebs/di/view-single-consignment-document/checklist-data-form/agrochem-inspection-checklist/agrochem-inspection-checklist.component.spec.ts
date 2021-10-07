import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgrochemInspectionChecklistComponent } from './agrochem-inspection-checklist.component';

describe('AgrochemInspectionChecklistComponent', () => {
  let component: AgrochemInspectionChecklistComponent;
  let fixture: ComponentFixture<AgrochemInspectionChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgrochemInspectionChecklistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgrochemInspectionChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
