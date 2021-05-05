import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrepareNwiComponent } from './prepare-nwi.component';

describe('PrepareNwiComponent', () => {
  let component: PrepareNwiComponent;
  let fixture: ComponentFixture<PrepareNwiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrepareNwiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrepareNwiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
