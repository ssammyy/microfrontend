import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NepUploadNotificationComponent } from './nep-upload-notification.component';

describe('NepUploadNotificationComponent', () => {
  let component: NepUploadNotificationComponent;
  let fixture: ComponentFixture<NepUploadNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NepUploadNotificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NepUploadNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
