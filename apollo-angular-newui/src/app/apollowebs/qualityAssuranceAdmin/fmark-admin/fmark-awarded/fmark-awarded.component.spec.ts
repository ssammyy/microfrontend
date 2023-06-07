import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkAwardedComponent } from './fmark-awarded.component';

describe('FmarkAwardedComponent', () => {
  let component: FmarkAwardedComponent;
  let fixture: ComponentFixture<FmarkAwardedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkAwardedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkAwardedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
