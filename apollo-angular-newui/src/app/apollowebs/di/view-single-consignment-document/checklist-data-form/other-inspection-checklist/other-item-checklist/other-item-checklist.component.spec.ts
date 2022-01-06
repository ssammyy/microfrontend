import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OtherItemChecklistComponent } from './other-item-checklist.component';

describe('OtherItemChecklistComponent', () => {
  let component: OtherItemChecklistComponent;
  let fixture: ComponentFixture<OtherItemChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OtherItemChecklistComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OtherItemChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
