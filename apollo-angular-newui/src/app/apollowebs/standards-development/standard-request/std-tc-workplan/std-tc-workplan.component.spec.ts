import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StdTcWorkplanComponent } from './std-tc-workplan.component';

describe('StdTcWorkplanComponent', () => {
  let component: StdTcWorkplanComponent;
  let fixture: ComponentFixture<StdTcWorkplanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StdTcWorkplanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StdTcWorkplanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
