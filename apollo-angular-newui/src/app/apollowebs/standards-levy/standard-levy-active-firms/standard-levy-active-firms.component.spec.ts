import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyActiveFirmsComponent } from './standard-levy-active-firms.component';

describe('StandardLevyActiveFirmsComponent', () => {
  let component: StandardLevyActiveFirmsComponent;
  let fixture: ComponentFixture<StandardLevyActiveFirmsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyActiveFirmsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyActiveFirmsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
