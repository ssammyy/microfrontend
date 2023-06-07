import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewSpcSecComponent } from './systemic-review-spc-sec.component';

describe('SystemicReviewSpcSecComponent', () => {
  let component: SystemicReviewSpcSecComponent;
  let fixture: ComponentFixture<SystemicReviewSpcSecComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewSpcSecComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewSpcSecComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
