import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaiverSubmittedDialogComponent } from './waiver-submitted-dialog.component';

describe('WaiverSubmittedDialogComponent', () => {
  let component: WaiverSubmittedDialogComponent;
  let fixture: ComponentFixture<WaiverSubmittedDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WaiverSubmittedDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WaiverSubmittedDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
