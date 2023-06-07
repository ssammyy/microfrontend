import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpcSecTaskComponent } from './spc-sec-task.component';

describe('SpcSecTaskComponent', () => {
  let component: SpcSecTaskComponent;
  let fixture: ComponentFixture<SpcSecTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpcSecTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpcSecTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
