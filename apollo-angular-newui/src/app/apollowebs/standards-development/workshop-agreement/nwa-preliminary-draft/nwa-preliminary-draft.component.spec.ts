import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaPreliminaryDraftComponent } from './nwa-preliminary-draft.component';

describe('NwaPreliminaryDraftComponent', () => {
  let component: NwaPreliminaryDraftComponent;
  let fixture: ComponentFixture<NwaPreliminaryDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaPreliminaryDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaPreliminaryDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
