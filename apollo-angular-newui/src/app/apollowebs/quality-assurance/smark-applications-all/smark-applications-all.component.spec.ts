import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkApplicationsAllComponent } from './smark-applications-all.component';

describe('SmarkApplicationsAllComponent', () => {
  let component: SmarkApplicationsAllComponent;
  let fixture: ComponentFixture<SmarkApplicationsAllComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkApplicationsAllComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkApplicationsAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
