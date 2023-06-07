import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdResponsesListComponent } from './int-std-responses-list.component';

describe('IntStdResponsesListComponent', () => {
  let component: IntStdResponsesListComponent;
  let fixture: ComponentFixture<IntStdResponsesListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdResponsesListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdResponsesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
