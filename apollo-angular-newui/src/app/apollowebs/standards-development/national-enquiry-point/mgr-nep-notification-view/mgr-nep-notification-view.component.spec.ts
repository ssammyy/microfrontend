import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MgrNepNotificationViewComponent } from './mgr-nep-notification-view.component';

describe('MgrNepNotificationViewComponent', () => {
  let component: MgrNepNotificationViewComponent;
  let fixture: ComponentFixture<MgrNepNotificationViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MgrNepNotificationViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MgrNepNotificationViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
