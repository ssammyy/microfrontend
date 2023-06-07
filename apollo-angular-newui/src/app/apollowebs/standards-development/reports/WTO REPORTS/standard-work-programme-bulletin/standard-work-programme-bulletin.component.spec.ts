import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardWorkProgrammeBulletinComponent } from './standard-work-programme-bulletin.component';

describe('StandardWorkProgrammeBulletinComponent', () => {
  let component: StandardWorkProgrammeBulletinComponent;
  let fixture: ComponentFixture<StandardWorkProgrammeBulletinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardWorkProgrammeBulletinComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardWorkProgrammeBulletinComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
