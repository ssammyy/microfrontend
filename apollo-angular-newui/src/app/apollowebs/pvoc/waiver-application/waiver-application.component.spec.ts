import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaiverApplicationComponent } from './waiver-application.component';

describe('WaiverApplicationComponent', () => {
  let component: WaiverApplicationComponent;
  let fixture: ComponentFixture<WaiverApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WaiverApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WaiverApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
