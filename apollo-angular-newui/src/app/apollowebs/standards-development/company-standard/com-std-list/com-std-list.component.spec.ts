import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdListComponent } from './com-std-list.component';

describe('ComStdListComponent', () => {
  let component: ComStdListComponent;
  let fixture: ComponentFixture<ComStdListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
