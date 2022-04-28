import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewExemptionDetailsComponent } from './view-exemption-details.component';

describe('ViewExemptionDetailsComponent', () => {
  let component: ViewExemptionDetailsComponent;
  let fixture: ComponentFixture<ViewExemptionDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewExemptionDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewExemptionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
