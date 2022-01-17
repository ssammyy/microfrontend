import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExceptionsApplicationComponent } from './exceptions-application.component';

describe('ExceptionsApplicationComponent', () => {
  let component: ExceptionsApplicationComponent;
  let fixture: ComponentFixture<ExceptionsApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExceptionsApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExceptionsApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
