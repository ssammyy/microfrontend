import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectedNwisComponent } from './rejected-nwis.component';

describe('RejectedNwisComponent', () => {
  let component: RejectedNwisComponent;
  let fixture: ComponentFixture<RejectedNwisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectedNwisComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RejectedNwisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
