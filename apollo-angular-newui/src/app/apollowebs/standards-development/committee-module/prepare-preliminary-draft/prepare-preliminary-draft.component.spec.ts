import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreparePreliminaryDraftComponent } from './prepare-preliminary-draft.component';

describe('PreparePreliminaryDraftComponent', () => {
  let component: PreparePreliminaryDraftComponent;
  let fixture: ComponentFixture<PreparePreliminaryDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreparePreliminaryDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreparePreliminaryDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
