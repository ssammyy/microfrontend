import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardTaskComponent } from './standard-task.component';

describe('StandardTaskComponent', () => {
  let component: StandardTaskComponent;
  let fixture: ComponentFixture<StandardTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
