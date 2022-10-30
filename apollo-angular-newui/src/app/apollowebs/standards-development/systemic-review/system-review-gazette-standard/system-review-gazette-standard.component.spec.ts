import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemReviewGazetteStandardComponent } from './system-review-gazette-standard.component';

describe('SystemReviewGazetteStandardComponent', () => {
  let component: SystemReviewGazetteStandardComponent;
  let fixture: ComponentFixture<SystemReviewGazetteStandardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemReviewGazetteStandardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemReviewGazetteStandardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
