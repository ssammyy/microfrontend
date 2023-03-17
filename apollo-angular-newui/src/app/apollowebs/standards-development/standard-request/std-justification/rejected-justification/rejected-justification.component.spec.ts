import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectedJustificationComponent } from './rejected-justification.component';

describe('RejectedJustificationComponent', () => {
  let component: RejectedJustificationComponent;
  let fixture: ComponentFixture<RejectedJustificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectedJustificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RejectedJustificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
