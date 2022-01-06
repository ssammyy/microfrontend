import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EngineeringItemChecklistComponent } from './engineering-item-checklist.component';

describe('EngineeringItemChecklistComponent', () => {
  let component: EngineeringItemChecklistComponent;
  let fixture: ComponentFixture<EngineeringItemChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EngineeringItemChecklistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EngineeringItemChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
