import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddUpdateFeeComponent} from './add-update-fee.component';

describe('AddUpdateFeeComponent', () => {
  let component: AddUpdateFeeComponent;
  let fixture: ComponentFixture<AddUpdateFeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddUpdateFeeComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUpdateFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
