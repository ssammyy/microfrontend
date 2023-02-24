import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaEditPreliminaryDraftComponent } from './nwa-edit-preliminary-draft.component';

describe('NwaEditPreliminaryDraftComponent', () => {
  let component: NwaEditPreliminaryDraftComponent;
  let fixture: ComponentFixture<NwaEditPreliminaryDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaEditPreliminaryDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaEditPreliminaryDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
