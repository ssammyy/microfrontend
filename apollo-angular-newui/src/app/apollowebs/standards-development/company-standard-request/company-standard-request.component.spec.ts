import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyStandardRequestComponent } from './company-standard-request.component';

describe('CompanyStandardRequestComponent', () => {
  let component: CompanyStandardRequestComponent;
  let fixture: ComponentFixture<CompanyStandardRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CompanyStandardRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyStandardRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
