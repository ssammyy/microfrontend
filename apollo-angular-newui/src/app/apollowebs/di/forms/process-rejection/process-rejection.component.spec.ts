import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessRejectionComponent } from './process-rejection.component';

describe('ProcessRejectionComponent', () => {
  let component: ProcessRejectionComponent;
  let fixture: ComponentFixture<ProcessRejectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProcessRejectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessRejectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
