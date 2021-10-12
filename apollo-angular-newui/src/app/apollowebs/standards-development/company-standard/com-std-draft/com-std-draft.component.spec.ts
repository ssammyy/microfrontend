import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdDraftComponent } from './com-std-draft.component';

describe('ComStdDraftComponent', () => {
  let component: ComStdDraftComponent;
  let fixture: ComponentFixture<ComStdDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
