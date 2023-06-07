import {ComponentFixture, TestBed} from '@angular/core/testing';

import {AddCustomOfficeComponent} from './add-custom-office.component';

describe('AddCustomOfficeComponent', () => {
  let component: AddCustomOfficeComponent;
  let fixture: ComponentFixture<AddCustomOfficeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddCustomOfficeComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCustomOfficeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
