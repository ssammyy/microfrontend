import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdEditDraftComponent } from './int-std-edit-draft.component';

describe('IntStdEditDraftComponent', () => {
  let component: IntStdEditDraftComponent;
  let fixture: ComponentFixture<IntStdEditDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdEditDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdEditDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
