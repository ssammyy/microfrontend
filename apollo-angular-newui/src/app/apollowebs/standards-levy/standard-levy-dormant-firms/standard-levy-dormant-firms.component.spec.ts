import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyDormantFirmsComponent } from './standard-levy-dormant-firms.component';

describe('StandardLevyDormantFirmsComponent', () => {
  let component: StandardLevyDormantFirmsComponent;
  let fixture: ComponentFixture<StandardLevyDormantFirmsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyDormantFirmsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyDormantFirmsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
