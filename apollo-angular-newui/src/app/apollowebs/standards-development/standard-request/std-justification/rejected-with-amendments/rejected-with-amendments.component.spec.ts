import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectedWithAmendmentsComponent } from './rejected-with-amendments.component';

describe('RejectedWithAmendmentsComponent', () => {
  let component: RejectedWithAmendmentsComponent;
  let fixture: ComponentFixture<RejectedWithAmendmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectedWithAmendmentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RejectedWithAmendmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
