import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkAllApplicationsComponent } from './fmark-all-applications.component';

describe('FmarkAllApplicationsComponent', () => {
  let component: FmarkAllApplicationsComponent;
  let fixture: ComponentFixture<FmarkAllApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkAllApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkAllApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
