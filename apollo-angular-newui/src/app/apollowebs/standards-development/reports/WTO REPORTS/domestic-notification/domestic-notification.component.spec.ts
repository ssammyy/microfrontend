import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomesticNotificationComponent } from './domestic-notification.component';

describe('DomesticNotificationComponent', () => {
  let component: DomesticNotificationComponent;
  let fixture: ComponentFixture<DomesticNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomesticNotificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DomesticNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
