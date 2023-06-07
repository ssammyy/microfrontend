import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewNscComponent } from './review-nsc.component';

describe('ReviewNscComponent', () => {
  let component: ReviewNscComponent;
  let fixture: ComponentFixture<ReviewNscComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewNscComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewNscComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
