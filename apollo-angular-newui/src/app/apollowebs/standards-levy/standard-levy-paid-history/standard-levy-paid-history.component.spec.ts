import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyPaidHistoryComponent } from './standard-levy-paid-history.component';

describe('StandardLevyPaidHistoryComponent', () => {
  let component: StandardLevyPaidHistoryComponent;
  let fixture: ComponentFixture<StandardLevyPaidHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyPaidHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyPaidHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
