import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevySiteVisitApproveTwoComponent } from './standard-levy-site-visit-approve-two.component';

describe('StandardLevySiteVisitApproveTwoComponent', () => {
  let component: StandardLevySiteVisitApproveTwoComponent;
  let fixture: ComponentFixture<StandardLevySiteVisitApproveTwoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevySiteVisitApproveTwoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevySiteVisitApproveTwoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
