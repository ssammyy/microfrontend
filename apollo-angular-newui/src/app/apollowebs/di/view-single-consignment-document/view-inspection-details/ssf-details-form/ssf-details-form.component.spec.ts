import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SsfDetailsFormComponent } from './ssf-details-form.component';

describe('SsfDetailsFormComponent', () => {
  let component: SsfDetailsFormComponent;
  let fixture: ComponentFixture<SsfDetailsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SsfDetailsFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SsfDetailsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
