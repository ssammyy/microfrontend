import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemeMembershipSicComponent } from './scheme-membership-sic.component';

describe('SchemeMembershipSicComponent', () => {
  let component: SchemeMembershipSicComponent;
  let fixture: ComponentFixture<SchemeMembershipSicComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchemeMembershipSicComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchemeMembershipSicComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
