import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StandardLevyPenaltiesComponent } from './standard-levy-penalties.component';

describe('StandardLevyPenaltiesComponent', () => {
  let component: StandardLevyPenaltiesComponent;
  let fixture: ComponentFixture<StandardLevyPenaltiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StandardLevyPenaltiesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StandardLevyPenaltiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
