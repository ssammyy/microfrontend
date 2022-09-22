import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyClosedFirmsComponent } from './standard-levy-closed-firms.component';

describe('StandardLevyClosedFirmsComponent', () => {
  let component: StandardLevyClosedFirmsComponent;
  let fixture: ComponentFixture<StandardLevyClosedFirmsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyClosedFirmsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyClosedFirmsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
