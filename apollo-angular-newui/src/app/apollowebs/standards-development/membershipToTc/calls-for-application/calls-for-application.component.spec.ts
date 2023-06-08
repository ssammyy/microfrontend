import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CallsForApplicationComponent } from './calls-for-application.component';

describe('CallsForApplicationComponent', () => {
  let component: CallsForApplicationComponent;
  let fixture: ComponentFixture<CallsForApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CallsForApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CallsForApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
