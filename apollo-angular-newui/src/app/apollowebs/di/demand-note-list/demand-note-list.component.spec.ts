import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DemandNoteListComponent } from './demand-note-list.component';

describe('DemandNoteListComponent', () => {
  let component: DemandNoteListComponent;
  let fixture: ComponentFixture<DemandNoteListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DemandNoteListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DemandNoteListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
