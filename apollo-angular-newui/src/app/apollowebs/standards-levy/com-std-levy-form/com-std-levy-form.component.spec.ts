import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStdLevyFormComponent } from './com-std-levy-form.component';

describe('ComStdLevyFormComponent', () => {
  let component: ComStdLevyFormComponent;
  let fixture: ComponentFixture<ComStdLevyFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStdLevyFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStdLevyFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
