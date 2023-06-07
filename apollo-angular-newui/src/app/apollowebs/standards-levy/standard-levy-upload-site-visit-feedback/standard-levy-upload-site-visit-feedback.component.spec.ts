import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyUploadSiteVisitFeedbackComponent } from './standard-levy-upload-site-visit-feedback.component';

describe('StandardLevyUploadSiteVisitFeedbackComponent', () => {
  let component: StandardLevyUploadSiteVisitFeedbackComponent;
  let fixture: ComponentFixture<StandardLevyUploadSiteVisitFeedbackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyUploadSiteVisitFeedbackComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyUploadSiteVisitFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
