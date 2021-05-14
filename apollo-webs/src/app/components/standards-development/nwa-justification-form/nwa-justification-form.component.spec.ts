import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaJustificationFormComponent } from './nwa-justification-form.component';

describe('NwaJustificationFormComponent', () => {
  let component: NwaJustificationFormComponent;
  let fixture: ComponentFixture<NwaJustificationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaJustificationFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaJustificationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
