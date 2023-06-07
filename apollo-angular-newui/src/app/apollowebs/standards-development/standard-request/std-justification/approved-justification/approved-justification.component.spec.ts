import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovedJustificationComponent } from './approved-justification.component';

describe('ApprovedJustificationComponent', () => {
  let component: ApprovedJustificationComponent;
  let fixture: ComponentFixture<ApprovedJustificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovedJustificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovedJustificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
