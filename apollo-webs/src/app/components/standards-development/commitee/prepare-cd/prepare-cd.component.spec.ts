import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrepareCdComponent } from './prepare-cd.component';

describe('PrepareCdComponent', () => {
  let component: PrepareCdComponent;
  let fixture: ComponentFixture<PrepareCdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrepareCdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrepareCdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
