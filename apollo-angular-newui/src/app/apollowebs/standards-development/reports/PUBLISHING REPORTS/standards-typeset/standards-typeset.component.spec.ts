import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardsTypesetComponent } from './standards-typeset.component';

describe('StandardsTypesetComponent', () => {
  let component: StandardsTypesetComponent;
  let fixture: ComponentFixture<StandardsTypesetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardsTypesetComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardsTypesetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
