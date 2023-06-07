import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdApproveDraftComponent } from './int-std-approve-draft.component';

describe('IntStdApproveDraftComponent', () => {
  let component: IntStdApproveDraftComponent;
  let fixture: ComponentFixture<IntStdApproveDraftComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdApproveDraftComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdApproveDraftComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
