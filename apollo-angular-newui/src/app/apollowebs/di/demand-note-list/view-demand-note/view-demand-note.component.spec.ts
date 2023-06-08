import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewDemandNoteComponent } from './view-demand-note.component';

describe('ViewDemandNoteComponent', () => {
  let component: ViewDemandNoteComponent;
  let fixture: ComponentFixture<ViewDemandNoteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewDemandNoteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewDemandNoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
