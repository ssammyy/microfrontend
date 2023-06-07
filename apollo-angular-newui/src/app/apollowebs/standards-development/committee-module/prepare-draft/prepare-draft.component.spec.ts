import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrepareDraftComponent } from './prepare-draft.component';

describe('PrepareDraftComponent', () => {
  let component: PrepareDraftComponent;
  let fixture: ComponentFixture<PrepareDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrepareDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrepareDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
