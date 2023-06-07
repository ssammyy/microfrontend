import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewStandardPublicationComponent } from './review-standard-publication.component';

describe('ReviewStandardPublicationComponent', () => {
  let component: ReviewStandardPublicationComponent;
  let fixture: ComponentFixture<ReviewStandardPublicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewStandardPublicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewStandardPublicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
