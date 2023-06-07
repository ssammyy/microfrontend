import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovedNwisComponent } from './approved-nwis.component';

describe('ApprovedNwisComponent', () => {
  let component: ApprovedNwisComponent;
  let fixture: ComponentFixture<ApprovedNwisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovedNwisComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovedNwisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
