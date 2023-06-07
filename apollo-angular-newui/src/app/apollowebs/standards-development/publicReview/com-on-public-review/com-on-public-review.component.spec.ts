import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComOnPublicReviewComponent } from './com-on-public-review.component';

describe('ComOnPublicReviewComponent', () => {
  let component: ComOnPublicReviewComponent;
  let fixture: ComponentFixture<ComOnPublicReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComOnPublicReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComOnPublicReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
