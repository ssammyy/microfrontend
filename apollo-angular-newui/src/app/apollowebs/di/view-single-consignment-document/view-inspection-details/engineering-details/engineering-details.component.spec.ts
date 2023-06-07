import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EngineeringDetailsComponent } from './engineering-details.component';

describe('EngineeringDetailsComponent', () => {
  let component: EngineeringDetailsComponent;
  let fixture: ComponentFixture<EngineeringDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EngineeringDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EngineeringDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
