import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemeMembershipFormComponent } from './scheme-membership-form.component';

describe('SchemeMembershipFormComponent', () => {
  let component: SchemeMembershipFormComponent;
  let fixture: ComponentFixture<SchemeMembershipFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SchemeMembershipFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SchemeMembershipFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
