import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdLevyApplicationsComponent } from './std-levy-applications.component';

describe('StdLevyApplicationsComponent', () => {
  let component: StdLevyApplicationsComponent;
  let fixture: ComponentFixture<StdLevyApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdLevyApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdLevyApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
