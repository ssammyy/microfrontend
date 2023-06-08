import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NepNotificationViewComponent } from './nep-notification-view.component';

describe('NepNotificationViewComponent', () => {
  let component: NepNotificationViewComponent;
  let fixture: ComponentFixture<NepNotificationViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NepNotificationViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NepNotificationViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
