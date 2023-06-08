import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyPenaltyHistoryComponent } from './standard-levy-penalty-history.component';

describe('StandardLevyPenaltyHistoryComponent', () => {
  let component: StandardLevyPenaltyHistoryComponent;
  let fixture: ComponentFixture<StandardLevyPenaltyHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyPenaltyHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyPenaltyHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
