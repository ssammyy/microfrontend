import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewEditorComponent } from './systemic-review-editor.component';

describe('SystemicReviewEditorComponent', () => {
  let component: SystemicReviewEditorComponent;
  let fixture: ComponentFixture<SystemicReviewEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
