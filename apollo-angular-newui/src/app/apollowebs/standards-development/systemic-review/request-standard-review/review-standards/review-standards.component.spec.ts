import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewStandardsComponent } from './review-standards.component';

describe('ReviewStandardsComponent', () => {
  let component: ReviewStandardsComponent;
  let fixture: ComponentFixture<ReviewStandardsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewStandardsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewStandardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
