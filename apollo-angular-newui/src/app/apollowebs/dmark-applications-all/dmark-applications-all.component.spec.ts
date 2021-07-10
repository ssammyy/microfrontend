import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkApplicationsAllComponent } from './dmark-applications-all.component';

describe('DmarkApplicationsAllComponent', () => {
  let component: DmarkApplicationsAllComponent;
  let fixture: ComponentFixture<DmarkApplicationsAllComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkApplicationsAllComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkApplicationsAllComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
