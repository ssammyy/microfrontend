import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewSmarkPermitComponent } from './new-smark-permit.component';

describe('NewSmarkPermitComponent', () => {
  let component: NewSmarkPermitComponent;
  let fixture: ComponentFixture<NewSmarkPermitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewSmarkPermitComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewSmarkPermitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
