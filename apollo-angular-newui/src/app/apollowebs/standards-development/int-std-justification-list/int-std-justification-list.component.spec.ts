import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdJustificationListComponent } from './int-std-justification-list.component';

describe('IntStdJustificationListComponent', () => {
  let component: IntStdJustificationListComponent;
  let fixture: ComponentFixture<IntStdJustificationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdJustificationListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdJustificationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
