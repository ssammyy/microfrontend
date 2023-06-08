import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardRequestComponent } from './standard-request.component';

describe('StandardRequestComponent', () => {
  let component: StandardRequestComponent;
  let fixture: ComponentFixture<StandardRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
