import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewTcSecComponent } from './systemic-review-tc-sec.component';

describe('SystemicReviewTcSecComponent', () => {
  let component: SystemicReviewTcSecComponent;
  let fixture: ComponentFixture<SystemicReviewTcSecComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewTcSecComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewTcSecComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
