import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermitDetailsAdminComponent } from './permit-details-admin.component';

describe('PermitDetailsAdminComponent', () => {
  let component: PermitDetailsAdminComponent;
  let fixture: ComponentFixture<PermitDetailsAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PermitDetailsAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PermitDetailsAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
