import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveRejectItemComponent } from './approve-reject-item.component';

describe('ApproveRejectItemComponent', () => {
  let component: ApproveRejectItemComponent;
  let fixture: ComponentFixture<ApproveRejectItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveRejectItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveRejectItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
