import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntStdListComponent } from './int-std-list.component';

describe('IntStdListComponent', () => {
  let component: IntStdListComponent;
  let fixture: ComponentFixture<IntStdListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntStdListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntStdListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
