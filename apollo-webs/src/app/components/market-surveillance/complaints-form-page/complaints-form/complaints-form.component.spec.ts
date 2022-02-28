import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ComplaintsFormComponent} from './complaints-form.component';

describe('ComplaintsFormComponent', () => {
  let component: ComplaintsFormComponent;
  let fixture: ComponentFixture<ComplaintsFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplaintsFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplaintsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
