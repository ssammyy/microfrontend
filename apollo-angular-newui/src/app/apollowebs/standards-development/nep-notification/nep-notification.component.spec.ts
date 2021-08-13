import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NepNotificationComponent } from './nep-notification.component';

describe('NepNotificationComponent', () => {
  let component: NepNotificationComponent;
  let fixture: ComponentFixture<NepNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NepNotificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NepNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
