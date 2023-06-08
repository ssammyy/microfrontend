import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsLevyQaPermitsComponent } from './standards-levy-qa-permits.component';

describe('StandardsLevyQaPermitsComponent', () => {
  let component: StandardsLevyQaPermitsComponent;
  let fixture: ComponentFixture<StandardsLevyQaPermitsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsLevyQaPermitsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsLevyQaPermitsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
