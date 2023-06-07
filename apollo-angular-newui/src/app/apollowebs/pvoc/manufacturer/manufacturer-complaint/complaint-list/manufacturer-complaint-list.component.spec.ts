import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ManufacturerComplaintListComponent} from './manufacturer-complaint-list.component';

describe('ManufacturerComplaintListComponent', () => {
  let component: ManufacturerComplaintListComponent;
  let fixture: ComponentFixture<ManufacturerComplaintListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ManufacturerComplaintListComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManufacturerComplaintListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
