import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewSacSecComponent } from './systemic-review-sac-sec.component';

describe('SystemicReviewSacSecComponent', () => {
  let component: SystemicReviewSacSecComponent;
  let fixture: ComponentFixture<SystemicReviewSacSecComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewSacSecComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewSacSecComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
