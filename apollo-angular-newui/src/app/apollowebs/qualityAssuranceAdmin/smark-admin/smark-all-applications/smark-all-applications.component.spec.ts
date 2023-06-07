import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkAllApplicationsComponent } from './smark-all-applications.component';

describe('SmarkAllApplicationsComponent', () => {
  let component: SmarkAllApplicationsComponent;
  let fixture: ComponentFixture<SmarkAllApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkAllApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkAllApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
