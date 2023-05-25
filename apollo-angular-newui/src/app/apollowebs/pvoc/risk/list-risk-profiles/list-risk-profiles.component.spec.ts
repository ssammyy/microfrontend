import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ListRiskProfilesComponent} from './list-risk-profiles.component';

describe('ListRiskProfilesComponent', () => {
  let component: ListRiskProfilesComponent;
  let fixture: ComponentFixture<ListRiskProfilesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListRiskProfilesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ListRiskProfilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
