import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaViewJustificationComponent } from './nwa-view-justification.component';

describe('NwaViewJustificationComponent', () => {
  let component: NwaViewJustificationComponent;
  let fixture: ComponentFixture<NwaViewJustificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaViewJustificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaViewJustificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
