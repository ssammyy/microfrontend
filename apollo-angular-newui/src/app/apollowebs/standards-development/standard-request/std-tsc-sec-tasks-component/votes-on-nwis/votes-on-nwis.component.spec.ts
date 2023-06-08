import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VotesOnNwisComponent } from './votes-on-nwis.component';

describe('VotesOnNwisComponent', () => {
  let component: VotesOnNwisComponent;
  let fixture: ComponentFixture<VotesOnNwisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VotesOnNwisComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VotesOnNwisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
