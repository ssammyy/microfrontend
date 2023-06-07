import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaiverNotApplicableComponent } from './waiver-not-applicable.component';

describe('WaiverNotApplicableComponent', () => {
  let component: WaiverNotApplicableComponent;
  let fixture: ComponentFixture<WaiverNotApplicableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WaiverNotApplicableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WaiverNotApplicableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
