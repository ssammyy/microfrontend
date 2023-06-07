import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsPrintedComponent } from './standards-printed.component';

describe('StandardsPrintedComponent', () => {
  let component: StandardsPrintedComponent;
  let fixture: ComponentFixture<StandardsPrintedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsPrintedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsPrintedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
