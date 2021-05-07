import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchememembershipComponent } from './schememembership.component';

describe('SchememembershipComponent', () => {
  let component: SchememembershipComponent;
  let fixture: ComponentFixture<SchememembershipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchememembershipComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchememembershipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
