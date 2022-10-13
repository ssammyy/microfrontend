import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermitsGrantedComponent } from './permits-granted.component';

describe('PermitsGrantedComponent', () => {
  let component: PermitsGrantedComponent;
  let fixture: ComponentFixture<PermitsGrantedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PermitsGrantedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PermitsGrantedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
