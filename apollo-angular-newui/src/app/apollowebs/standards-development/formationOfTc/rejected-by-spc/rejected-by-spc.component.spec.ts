import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectedBySpcComponent } from './rejected-by-spc.component';

describe('RejectedBySpcComponent', () => {
  let component: RejectedBySpcComponent;
  let fixture: ComponentFixture<RejectedBySpcComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectedBySpcComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RejectedBySpcComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
