import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsLevyHomeComponent } from './standards-levy-home.component';

describe('StandardsLevyHomeComponent', () => {
  let component: StandardsLevyHomeComponent;
  let fixture: ComponentFixture<StandardsLevyHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsLevyHomeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsLevyHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
