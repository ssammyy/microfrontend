import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkAdminComponent } from './dmark-admin.component';

describe('DmarkAdminComponent', () => {
  let component: DmarkAdminComponent;
  let fixture: ComponentFixture<DmarkAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
