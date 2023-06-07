import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectedForNwiComponent } from './rejected-for-nwi.component';

describe('RejectedForNwiComponent', () => {
  let component: RejectedForNwiComponent;
  let fixture: ComponentFixture<RejectedForNwiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectedForNwiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RejectedForNwiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
