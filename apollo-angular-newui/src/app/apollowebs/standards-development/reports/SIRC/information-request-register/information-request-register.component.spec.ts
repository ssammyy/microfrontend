import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InformationRequestRegisterComponent } from './information-request-register.component';

describe('InformationRequestRegisterComponent', () => {
  let component: InformationRequestRegisterComponent;
  let fixture: ComponentFixture<InformationRequestRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InformationRequestRegisterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InformationRequestRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
