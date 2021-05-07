import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HodjoinrequestComponent } from './hodjoinrequest.component';

describe('HodjoinrequestComponent', () => {
  let component: HodjoinrequestComponent;
  let fixture: ComponentFixture<HodjoinrequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HodjoinrequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HodjoinrequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
