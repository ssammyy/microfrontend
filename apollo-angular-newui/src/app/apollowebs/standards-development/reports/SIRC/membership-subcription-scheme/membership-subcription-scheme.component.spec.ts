import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembershipSubcriptionSchemeComponent } from './membership-subcription-scheme.component';

describe('MembershipSubcriptionSchemeComponent', () => {
  let component: MembershipSubcriptionSchemeComponent;
  let fixture: ComponentFixture<MembershipSubcriptionSchemeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MembershipSubcriptionSchemeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MembershipSubcriptionSchemeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
