import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkAdminComponent } from './fmark-admin.component';

describe('FmarkAdminComponent', () => {
  let component: FmarkAdminComponent;
  let fixture: ComponentFixture<FmarkAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FmarkAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
