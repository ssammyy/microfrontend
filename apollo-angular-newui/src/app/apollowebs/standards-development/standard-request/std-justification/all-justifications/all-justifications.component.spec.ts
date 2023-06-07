import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllJustificationsComponent } from './all-justifications.component';

describe('AllJustificationsComponent', () => {
  let component: AllJustificationsComponent;
  let fixture: ComponentFixture<AllJustificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllJustificationsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllJustificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
