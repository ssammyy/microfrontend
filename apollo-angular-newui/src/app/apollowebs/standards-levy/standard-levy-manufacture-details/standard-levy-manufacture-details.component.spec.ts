import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyManufactureDetailsComponent } from './standard-levy-manufacture-details.component';

describe('StandardLevyManufactureDetailsComponent', () => {
  let component: StandardLevyManufactureDetailsComponent;
  let fixture: ComponentFixture<StandardLevyManufactureDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyManufactureDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyManufactureDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
