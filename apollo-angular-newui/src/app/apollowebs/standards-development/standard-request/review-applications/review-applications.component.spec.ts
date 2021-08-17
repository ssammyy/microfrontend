import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewApplicationsComponent } from './review-applications.component';

describe('ReviewApplicationsComponent', () => {
  let component: ReviewApplicationsComponent;
  let fixture: ComponentFixture<ReviewApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
