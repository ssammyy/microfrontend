import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdAppDraftComponent } from './com-std-app-draft.component';

describe('ComStdAppDraftComponent', () => {
  let component: ComStdAppDraftComponent;
  let fixture: ComponentFixture<ComStdAppDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdAppDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdAppDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
