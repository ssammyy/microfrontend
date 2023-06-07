import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddUpdateFeeRangeComponent} from './add-update-fee-range.component';

describe('AddUpdateFeeRangeComponent', () => {
  let component: AddUpdateFeeRangeComponent;
  let fixture: ComponentFixture<AddUpdateFeeRangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddUpdateFeeRangeComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUpdateFeeRangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
