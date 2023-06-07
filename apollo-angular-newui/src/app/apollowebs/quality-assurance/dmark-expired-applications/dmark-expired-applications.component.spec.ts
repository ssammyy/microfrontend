import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkExpiredApplicationsComponent } from './dmark-expired-applications.component';

describe('DmarkExpiredApplicationsComponent', () => {
  let component: DmarkExpiredApplicationsComponent;
  let fixture: ComponentFixture<DmarkExpiredApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkExpiredApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkExpiredApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
