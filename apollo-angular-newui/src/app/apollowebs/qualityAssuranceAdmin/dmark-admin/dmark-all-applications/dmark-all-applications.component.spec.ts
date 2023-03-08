import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkAllApplicationsComponent } from './dmark-all-applications.component';

describe('DmarkAllApplicationsComponent', () => {
  let component: DmarkAllApplicationsComponent;
  let fixture: ComponentFixture<DmarkAllApplicationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkAllApplicationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkAllApplicationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
