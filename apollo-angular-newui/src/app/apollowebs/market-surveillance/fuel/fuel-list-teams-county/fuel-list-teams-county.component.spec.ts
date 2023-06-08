import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelListTeamsCountyComponent } from './fuel-list-teams-county.component';

describe('FuelListTeamsCountyComponent', () => {
  let component: FuelListTeamsCountyComponent;
  let fixture: ComponentFixture<FuelListTeamsCountyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FuelListTeamsCountyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FuelListTeamsCountyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
