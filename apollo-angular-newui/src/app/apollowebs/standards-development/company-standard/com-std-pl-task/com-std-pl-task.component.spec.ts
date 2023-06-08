import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdPlTaskComponent } from './com-std-pl-task.component';

describe('ComStdPlTaskComponent', () => {
  let component: ComStdPlTaskComponent;
  let fixture: ComponentFixture<ComStdPlTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdPlTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdPlTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
