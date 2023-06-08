import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrepareCommitteeDraftComponent } from './prepare-committee-draft.component';

describe('PrepareCommitteeDraftComponent', () => {
  let component: PrepareCommitteeDraftComponent;
  let fixture: ComponentFixture<PrepareCommitteeDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrepareCommitteeDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrepareCommitteeDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
