import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdDraftCommentsComponent } from './com-std-draft-comments.component';

describe('ComStdDraftCommentsComponent', () => {
  let component: ComStdDraftCommentsComponent;
  let fixture: ComponentFixture<ComStdDraftCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdDraftCommentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdDraftCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
