import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OnholdForNwiComponent } from './onhold-for-nwi.component';

describe('OnholdForNwiComponent', () => {
  let component: OnholdForNwiComponent;
  let fixture: ComponentFixture<OnholdForNwiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OnholdForNwiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OnholdForNwiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
