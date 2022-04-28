import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewWaiverDetailsComponent } from './view-waiver-details.component';

describe('ViewWaiverDetailsComponent', () => {
  let component: ViewWaiverDetailsComponent;
  let fixture: ComponentFixture<ViewWaiverDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewWaiverDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewWaiverDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
