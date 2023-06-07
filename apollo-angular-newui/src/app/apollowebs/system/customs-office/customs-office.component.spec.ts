import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CustomsOfficeComponent} from './customs-office.component';

describe('CustomsOfficeComponent', () => {
  let component: CustomsOfficeComponent;
  let fixture: ComponentFixture<CustomsOfficeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomsOfficeComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomsOfficeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
