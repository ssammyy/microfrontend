import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkExpiredApplicationsComponent } from './fmark-expired-applications.component';

describe('FmarkExpiredApplicationsComponent', () => {
  let component: FmarkExpiredApplicationsComponent;
  let fixture: ComponentFixture<FmarkExpiredApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkExpiredApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkExpiredApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
