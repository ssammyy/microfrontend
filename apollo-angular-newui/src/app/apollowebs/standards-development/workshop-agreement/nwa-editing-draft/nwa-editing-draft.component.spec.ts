import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaEditingDraftComponent } from './nwa-editing-draft.component';

describe('NwaEditingDraftComponent', () => {
  let component: NwaEditingDraftComponent;
  let fixture: ComponentFixture<NwaEditingDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaEditingDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaEditingDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
