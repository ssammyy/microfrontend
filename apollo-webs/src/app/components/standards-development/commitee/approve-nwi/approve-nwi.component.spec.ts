import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveNwiComponent } from './approve-nwi.component';

describe('ApproveNwiComponent', () => {
  let component: ApproveNwiComponent;
  let fixture: ComponentFixture<ApproveNwiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveNwiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveNwiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
