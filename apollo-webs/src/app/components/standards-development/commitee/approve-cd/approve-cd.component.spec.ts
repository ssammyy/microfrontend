import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveCdComponent } from './approve-cd.component';

describe('ApproveCdComponent', () => {
  let component: ApproveCdComponent;
  let fixture: ComponentFixture<ApproveCdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveCdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveCdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
