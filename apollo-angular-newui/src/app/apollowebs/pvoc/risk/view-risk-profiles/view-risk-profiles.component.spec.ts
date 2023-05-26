import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewRiskProfilesComponent} from './view-risk-profiles.component';

describe('ViewRiskProfilesComponent', () => {
  let component: ViewRiskProfilesComponent;
  let fixture: ComponentFixture<ViewRiskProfilesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewRiskProfilesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewRiskProfilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
