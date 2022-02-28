import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ComplaintsFormPageComponent} from './complaints-form-page.component';

describe('ComplaintsFormPageComponent', () => {
  let component: ComplaintsFormPageComponent;
  let fixture: ComponentFixture<ComplaintsFormPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplaintsFormPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplaintsFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
