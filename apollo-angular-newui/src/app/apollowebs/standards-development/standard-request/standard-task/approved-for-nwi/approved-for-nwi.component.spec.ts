import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovedForNwiComponent } from './approved-for-nwi.component';

describe('ApprovedForNwiComponent', () => {
  let component: ApprovedForNwiComponent;
  let fixture: ComponentFixture<ApprovedForNwiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovedForNwiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovedForNwiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
