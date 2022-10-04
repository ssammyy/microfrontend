import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddLaboratoryRequestComponent } from './add-laboratory-request.component';

describe('AddLaboratoryRequestComponent', () => {
  let component: AddLaboratoryRequestComponent;
  let fixture: ComponentFixture<AddLaboratoryRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddLaboratoryRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddLaboratoryRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
