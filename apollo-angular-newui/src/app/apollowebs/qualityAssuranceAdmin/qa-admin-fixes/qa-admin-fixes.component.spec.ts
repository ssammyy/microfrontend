import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QaAdminFixesComponent } from './qa-admin-fixes.component';

describe('QaAdminFixesComponent', () => {
  let component: QaAdminFixesComponent;
  let fixture: ComponentFixture<QaAdminFixesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QaAdminFixesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QaAdminFixesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
