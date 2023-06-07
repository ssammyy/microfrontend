import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemicReviewHopComponent } from './systemic-review-hop.component';

describe('SystemicReviewHopComponent', () => {
  let component: SystemicReviewHopComponent;
  let fixture: ComponentFixture<SystemicReviewHopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemicReviewHopComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemicReviewHopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
