import { ComponentFixture, TestBed } from '@angular/core/testing';

import { St10FormComponent } from './st10-form.component';

describe('St10FormComponent', () => {
  let component: St10FormComponent;
  let fixture: ComponentFixture<St10FormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ St10FormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(St10FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
