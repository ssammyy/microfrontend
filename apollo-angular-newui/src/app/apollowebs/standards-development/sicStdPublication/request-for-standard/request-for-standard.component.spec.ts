import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestForStandardComponent } from './request-for-standard.component';

describe('RequestForStandardComponent', () => {
  let component: RequestForStandardComponent;
  let fixture: ComponentFixture<RequestForStandardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestForStandardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestForStandardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
