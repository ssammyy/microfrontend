import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyClosureComponent } from './standard-levy-closure.component';

describe('StandardLevyClosureComponent', () => {
  let component: StandardLevyClosureComponent;
  let fixture: ComponentFixture<StandardLevyClosureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyClosureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyClosureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
