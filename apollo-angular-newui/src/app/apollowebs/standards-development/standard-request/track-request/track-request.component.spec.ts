import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackRequestComponent } from './track-request.component';

describe('TrackRequestComponent', () => {
  let component: TrackRequestComponent;
  let fixture: ComponentFixture<TrackRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrackRequestComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrackRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
