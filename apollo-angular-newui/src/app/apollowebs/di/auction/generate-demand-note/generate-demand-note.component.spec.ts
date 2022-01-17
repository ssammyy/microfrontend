import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateDemandNoteComponent } from './generate-demand-note.component';

describe('GenerateDemandNoteComponent', () => {
  let component: GenerateDemandNoteComponent;
  let fixture: ComponentFixture<GenerateDemandNoteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GenerateDemandNoteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateDemandNoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
