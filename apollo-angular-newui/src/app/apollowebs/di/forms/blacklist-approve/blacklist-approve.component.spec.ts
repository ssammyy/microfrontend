import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlacklistApproveComponent } from './blacklist-approve.component';

describe('BlacklistApproveComponent', () => {
  let component: BlacklistApproveComponent;
  let fixture: ComponentFixture<BlacklistApproveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BlacklistApproveComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BlacklistApproveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
