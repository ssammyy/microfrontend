import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaJustificationTasksComponent } from './nwa-justification-tasks.component';

describe('NwaJustificationTasksComponent', () => {
  let component: NwaJustificationTasksComponent;
  let fixture: ComponentFixture<NwaJustificationTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaJustificationTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaJustificationTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
