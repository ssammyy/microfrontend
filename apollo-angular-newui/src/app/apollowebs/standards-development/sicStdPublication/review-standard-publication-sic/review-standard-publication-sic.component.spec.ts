import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewStandardPublicationSicComponent } from './review-standard-publication-sic.component';

describe('ReviewStandardPublicationSicComponent', () => {
  let component: ReviewStandardPublicationSicComponent;
  let fixture: ComponentFixture<ReviewStandardPublicationSicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewStandardPublicationSicComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewStandardPublicationSicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
