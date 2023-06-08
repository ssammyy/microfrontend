import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuelListTeamsComponent } from './fuel-list-teams.component';

describe('FuelListTeamsComponent', () => {
  let component: FuelListTeamsComponent;
  let fixture: ComponentFixture<FuelListTeamsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FuelListTeamsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FuelListTeamsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
