import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdJustificationComponent } from './std-justification.component';

describe('StdJustificationComponent', () => {
  let component: StdJustificationComponent;
  let fixture: ComponentFixture<StdJustificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdJustificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdJustificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
