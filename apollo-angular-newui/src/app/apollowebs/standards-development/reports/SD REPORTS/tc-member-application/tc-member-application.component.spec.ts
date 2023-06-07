import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TcMemberApplicationComponent } from './tc-member-application.component';

describe('TcMemberApplicationComponent', () => {
  let component: TcMemberApplicationComponent;
  let fixture: ComponentFixture<TcMemberApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TcMemberApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TcMemberApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
