import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdJcJustificationComponent } from './com-std-jc-justification.component';

describe('ComStdJcJustificationComponent', () => {
  let component: ComStdJcJustificationComponent;
  let fixture: ComponentFixture<ComStdJcJustificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdJcJustificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdJcJustificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
