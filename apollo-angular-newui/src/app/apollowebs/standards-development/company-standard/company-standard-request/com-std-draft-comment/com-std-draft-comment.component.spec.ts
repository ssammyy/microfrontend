import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdDraftCommentComponent } from './com-std-draft-comment.component';

describe('ComStdDraftCommentComponent', () => {
  let component: ComStdDraftCommentComponent;
  let fixture: ComponentFixture<ComStdDraftCommentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdDraftCommentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdDraftCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
