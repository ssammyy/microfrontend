import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkExpiredApplicationsComponent } from './smark-expired-applications.component';

describe('SmarkExpiredApplicationsComponent', () => {
  let component: SmarkExpiredApplicationsComponent;
  let fixture: ComponentFixture<SmarkExpiredApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkExpiredApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkExpiredApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
