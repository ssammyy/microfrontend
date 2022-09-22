import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyAllPaymentsComponent } from './standard-levy-all-payments.component';

describe('StandardLevyAllPaymentsComponent', () => {
  let component: StandardLevyAllPaymentsComponent;
  let fixture: ComponentFixture<StandardLevyAllPaymentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyAllPaymentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyAllPaymentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
