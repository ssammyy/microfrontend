import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentCdComponent } from './comment-cd.component';

describe('CommentListComponent', () => {
  let component: CommentCdComponent;
  let fixture: ComponentFixture<CommentCdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommentCdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentCdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
