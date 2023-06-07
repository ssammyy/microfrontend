import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TargetApproveItemComponent } from './target-approve-item.component';

describe('TargetApproveItemComponent', () => {
  let component: TargetApproveItemComponent;
  let fixture: ComponentFixture<TargetApproveItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TargetApproveItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TargetApproveItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
