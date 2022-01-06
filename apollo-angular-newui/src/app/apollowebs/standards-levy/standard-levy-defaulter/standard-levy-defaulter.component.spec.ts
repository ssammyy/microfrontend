import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyDefaulterComponent } from './standard-levy-defaulter.component';

describe('StandardLevyDefaulterComponent', () => {
  let component: StandardLevyDefaulterComponent;
  let fixture: ComponentFixture<StandardLevyDefaulterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyDefaulterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyDefaulterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
