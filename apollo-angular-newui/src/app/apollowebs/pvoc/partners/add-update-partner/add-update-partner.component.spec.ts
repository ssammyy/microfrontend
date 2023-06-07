import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUpdatePartnerComponent } from './add-update-partner.component';

describe('AddUpdatePartnerComponent', () => {
  let component: AddUpdatePartnerComponent;
  let fixture: ComponentFixture<AddUpdatePartnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddUpdatePartnerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddUpdatePartnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
