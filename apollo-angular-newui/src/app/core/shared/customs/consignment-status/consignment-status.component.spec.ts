import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsignmentStatusComponent } from './consignment-status.component';

describe('ConsignmentStatusComponent', () => {
  let component: ConsignmentStatusComponent;
  let fixture: ComponentFixture<ConsignmentStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsignmentStatusComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsignmentStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
