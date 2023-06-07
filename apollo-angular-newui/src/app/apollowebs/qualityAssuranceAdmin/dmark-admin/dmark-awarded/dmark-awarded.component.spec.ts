import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkAwardedComponent } from './dmark-awarded.component';

describe('DmarkAwardedComponent', () => {
  let component: DmarkAwardedComponent;
  let fixture: ComponentFixture<DmarkAwardedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkAwardedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkAwardedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
