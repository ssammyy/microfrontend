import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevySiteVisitApproveOneComponent } from './standard-levy-site-visit-approve-one.component';

describe('StandardLevySiteVisitApproveOneComponent', () => {
  let component: StandardLevySiteVisitApproveOneComponent;
  let fixture: ComponentFixture<StandardLevySiteVisitApproveOneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevySiteVisitApproveOneComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevySiteVisitApproveOneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
