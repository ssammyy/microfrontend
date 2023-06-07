import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdDraftViewComponent } from './com-std-draft-view.component';

describe('ComStdDraftViewComponent', () => {
  let component: ComStdDraftViewComponent;
  let fixture: ComponentFixture<ComStdDraftViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdDraftViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdDraftViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
