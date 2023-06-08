import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MinistryInspectionRequestComponent } from './ministry-inspection-request.component';

describe('MinistryInspectionRequestComponent', () => {
  let component: MinistryInspectionRequestComponent;
  let fixture: ComponentFixture<MinistryInspectionRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MinistryInspectionRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MinistryInspectionRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
