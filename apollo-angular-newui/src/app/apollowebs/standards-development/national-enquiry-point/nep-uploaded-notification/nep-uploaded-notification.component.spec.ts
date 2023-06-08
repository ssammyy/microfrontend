import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NepUploadedNotificationComponent } from './nep-uploaded-notification.component';

describe('NepUploadedNotificationComponent', () => {
  let component: NepUploadedNotificationComponent;
  let fixture: ComponentFixture<NepUploadedNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NepUploadedNotificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NepUploadedNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
