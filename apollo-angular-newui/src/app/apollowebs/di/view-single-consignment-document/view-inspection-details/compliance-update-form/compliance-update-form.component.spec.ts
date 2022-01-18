import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComplianceUpdateFormComponent } from './compliance-update-form.component';

describe('ComplianceUpdateFormComponent', () => {
  let component: ComplianceUpdateFormComponent;
  let fixture: ComponentFixture<ComplianceUpdateFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComplianceUpdateFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComplianceUpdateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
