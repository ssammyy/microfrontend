import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagernotificationsComponent } from './managernotifications.component';

describe('ManagernotificationsComponent', () => {
  let component: ManagernotificationsComponent;
  let fixture: ComponentFixture<ManagernotificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManagernotificationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManagernotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
