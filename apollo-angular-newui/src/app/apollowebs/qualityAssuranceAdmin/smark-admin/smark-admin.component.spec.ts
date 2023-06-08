import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkAdminComponent } from './smark-admin.component';

describe('SmarkAdminComponent', () => {
  let component: SmarkAdminComponent;
  let fixture: ComponentFixture<SmarkAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SmarkAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
