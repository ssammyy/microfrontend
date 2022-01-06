import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestForFormationOfTCComponent } from './request-for-formation-of-tc.component';

describe('RequestForFormationOfTCComponent', () => {
  let component: RequestForFormationOfTCComponent;
  let fixture: ComponentFixture<RequestForFormationOfTCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestForFormationOfTCComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestForFormationOfTCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
