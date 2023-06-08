import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsDevelopmentComponent } from './standards-development.component';

describe('StandardsDevelopmentComponent', () => {
  let component: StandardsDevelopmentComponent;
  let fixture: ComponentFixture<StandardsDevelopmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsDevelopmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsDevelopmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
