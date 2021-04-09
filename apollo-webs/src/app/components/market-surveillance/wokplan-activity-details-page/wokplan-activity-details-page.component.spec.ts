import {ComponentFixture, TestBed} from '@angular/core/testing';

import {WokplanActivityDetailsPageComponent} from './wokplan-activity-details-page.component';

describe('WokplanActivityDetailsPageComponent', () => {
  let component: WokplanActivityDetailsPageComponent;
  let fixture: ComponentFixture<WokplanActivityDetailsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WokplanActivityDetailsPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WokplanActivityDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
