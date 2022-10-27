import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemReviewUpdateGazetteComponent } from './system-review-update-gazette.component';

describe('SystemReviewUpdateGazetteComponent', () => {
  let component: SystemReviewUpdateGazetteComponent;
  let fixture: ComponentFixture<SystemReviewUpdateGazetteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemReviewUpdateGazetteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemReviewUpdateGazetteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
