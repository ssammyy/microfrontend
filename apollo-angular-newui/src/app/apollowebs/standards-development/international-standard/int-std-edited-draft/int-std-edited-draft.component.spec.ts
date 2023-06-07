import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdEditedDraftComponent } from './int-std-edited-draft.component';

describe('IntStdEditedDraftComponent', () => {
  let component: IntStdEditedDraftComponent;
  let fixture: ComponentFixture<IntStdEditedDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdEditedDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdEditedDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
