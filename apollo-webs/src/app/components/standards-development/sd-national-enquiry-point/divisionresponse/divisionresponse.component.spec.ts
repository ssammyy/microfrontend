import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DivisionresponseComponent } from './divisionresponse.component';

describe('DivisionresponseComponent', () => {
  let component: DivisionresponseComponent;
  let fixture: ComponentFixture<DivisionresponseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DivisionresponseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DivisionresponseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
