import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ManufacturerComplaintDetailsComponent} from './manufacturer-complaint-details.component';

describe('ManufacturerComplaintDetailsComponent', () => {
  let component: ManufacturerComplaintDetailsComponent;
  let fixture: ComponentFixture<ManufacturerComplaintDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManufacturerComplaintDetailsComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManufacturerComplaintDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
