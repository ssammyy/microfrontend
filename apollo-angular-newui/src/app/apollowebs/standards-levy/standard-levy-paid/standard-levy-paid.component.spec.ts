import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyPaidComponent } from './standard-levy-paid.component';

describe('StandardLevyPaidComponent', () => {
  let component: StandardLevyPaidComponent;
  let fixture: ComponentFixture<StandardLevyPaidComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyPaidComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyPaidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
