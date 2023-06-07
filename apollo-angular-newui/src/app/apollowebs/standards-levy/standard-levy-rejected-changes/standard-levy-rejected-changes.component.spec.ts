import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyRejectedChangesComponent } from './standard-levy-rejected-changes.component';

describe('StandardLevyRejectedChangesComponent', () => {
  let component: StandardLevyRejectedChangesComponent;
  let fixture: ComponentFixture<StandardLevyRejectedChangesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyRejectedChangesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyRejectedChangesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
