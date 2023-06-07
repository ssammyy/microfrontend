import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyRegisteredFirmsComponent } from './standard-levy-registered-firms.component';

describe('StandardLevyRegisteredFirmsComponent', () => {
  let component: StandardLevyRegisteredFirmsComponent;
  let fixture: ComponentFixture<StandardLevyRegisteredFirmsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyRegisteredFirmsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyRegisteredFirmsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
