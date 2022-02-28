import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreparePRCommentComponent } from './prepare-prcomment.component';

describe('PreparePRCommentComponent', () => {
  let component: PreparePRCommentComponent;
  let fixture: ComponentFixture<PreparePRCommentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreparePRCommentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreparePRCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
