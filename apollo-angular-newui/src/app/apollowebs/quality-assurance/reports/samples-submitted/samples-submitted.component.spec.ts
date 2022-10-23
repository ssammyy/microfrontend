import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SamplesSubmittedComponent } from './samples-submitted.component';

describe('SamplesSubmittedComponent', () => {
  let component: SamplesSubmittedComponent;
  let fixture: ComponentFixture<SamplesSubmittedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SamplesSubmittedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SamplesSubmittedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
