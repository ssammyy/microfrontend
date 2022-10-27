import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewProofReaderComponent } from './systemic-review-proof-reader.component';

describe('SystemicReviewProofReaderComponent', () => {
  let component: SystemicReviewProofReaderComponent;
  let fixture: ComponentFixture<SystemicReviewProofReaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewProofReaderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewProofReaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
