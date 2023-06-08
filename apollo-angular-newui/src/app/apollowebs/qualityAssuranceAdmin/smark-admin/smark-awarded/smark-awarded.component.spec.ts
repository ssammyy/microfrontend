import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkAwardedComponent } from './smark-awarded.component';

describe('SmarkAwardedComponent', () => {
  let component: SmarkAwardedComponent;
  let fixture: ComponentFixture<SmarkAwardedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkAwardedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkAwardedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
