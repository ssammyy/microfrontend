import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevySiteVisitComponent } from './standard-levy-site-visit.component';

describe('StandardLevySiteVisitComponent', () => {
  let component: StandardLevySiteVisitComponent;
  let fixture: ComponentFixture<StandardLevySiteVisitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevySiteVisitComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevySiteVisitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
