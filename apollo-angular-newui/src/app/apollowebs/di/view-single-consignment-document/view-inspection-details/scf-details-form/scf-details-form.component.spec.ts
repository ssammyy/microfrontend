import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScfDetailsFormComponent } from './scf-details-form.component';

describe('ScfDetailsFormComponent', () => {
  let component: ScfDetailsFormComponent;
  let fixture: ComponentFixture<ScfDetailsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScfDetailsFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScfDetailsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
