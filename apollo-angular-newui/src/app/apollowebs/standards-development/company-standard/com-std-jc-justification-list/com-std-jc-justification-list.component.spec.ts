import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdJcJustificationListComponent } from './com-std-jc-justification-list.component';

describe('ComStdJcJustificationListComponent', () => {
  let component: ComStdJcJustificationListComponent;
  let fixture: ComponentFixture<ComStdJcJustificationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdJcJustificationListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdJcJustificationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
