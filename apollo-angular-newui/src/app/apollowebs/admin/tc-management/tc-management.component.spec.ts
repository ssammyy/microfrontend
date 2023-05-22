import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TcManagementComponent } from './tc-management.component';

describe('TcManagementComponent', () => {
  let component: TcManagementComponent;
  let fixture: ComponentFixture<TcManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TcManagementComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TcManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
