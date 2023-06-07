import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyDefaulterHistoryComponent } from './standard-levy-defaulter-history.component';

describe('StandardLevyDefaulterHistoryComponent', () => {
  let component: StandardLevyDefaulterHistoryComponent;
  let fixture: ComponentFixture<StandardLevyDefaulterHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyDefaulterHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyDefaulterHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
