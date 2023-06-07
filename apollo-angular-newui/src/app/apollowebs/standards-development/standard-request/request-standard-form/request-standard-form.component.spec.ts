import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestStandardFormComponent } from './request-standard-form.component';

describe('RequestStandardFormComponent', () => {
  let component: RequestStandardFormComponent;
  let fixture: ComponentFixture<RequestStandardFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestStandardFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestStandardFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
