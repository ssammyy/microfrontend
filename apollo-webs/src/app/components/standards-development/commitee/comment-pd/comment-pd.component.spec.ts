import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentPdComponent } from './comment-pd.component';

describe('CommentPdComponent', () => {
  let component: CommentPdComponent;
  let fixture: ComponentFixture<CommentPdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommentPdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommentPdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
