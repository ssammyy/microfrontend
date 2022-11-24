import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdDraughtComponent } from './int-std-draught.component';

describe('IntStdDraughtComponent', () => {
  let component: IntStdDraughtComponent;
  let fixture: ComponentFixture<IntStdDraughtComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdDraughtComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdDraughtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
