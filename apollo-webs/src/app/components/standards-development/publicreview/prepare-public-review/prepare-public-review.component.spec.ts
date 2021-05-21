import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreparePublicReviewComponent } from './prepare-public-review.component';

describe('PreparePublicReviewComponent', () => {
  let component: PreparePublicReviewComponent;
  let fixture: ComponentFixture<PreparePublicReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreparePublicReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreparePublicReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
