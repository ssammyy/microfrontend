import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsEditedComponent } from './standards-edited.component';

describe('StandardsEditedComponent', () => {
  let component: StandardsEditedComponent;
  let fixture: ComponentFixture<StandardsEditedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsEditedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsEditedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
