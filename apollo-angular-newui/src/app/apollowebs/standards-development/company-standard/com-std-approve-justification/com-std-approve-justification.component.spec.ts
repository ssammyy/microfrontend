import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdApproveJustificationComponent } from './com-std-approve-justification.component';

describe('ComStdApproveJustificationComponent', () => {
  let component: ComStdApproveJustificationComponent;
  let fixture: ComponentFixture<ComStdApproveJustificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdApproveJustificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdApproveJustificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
