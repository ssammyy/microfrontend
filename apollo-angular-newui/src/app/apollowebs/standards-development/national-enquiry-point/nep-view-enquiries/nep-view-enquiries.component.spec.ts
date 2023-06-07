import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NepViewEnquiriesComponent } from './nep-view-enquiries.component';

describe('NepViewEnquiriesComponent', () => {
  let component: NepViewEnquiriesComponent;
  let fixture: ComponentFixture<NepViewEnquiriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NepViewEnquiriesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NepViewEnquiriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
