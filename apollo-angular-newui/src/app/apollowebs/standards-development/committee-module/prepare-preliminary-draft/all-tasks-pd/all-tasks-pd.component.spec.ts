import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllTasksPdComponent } from './all-tasks-pd.component';

describe('AllTasksPdComponent', () => {
  let component: AllTasksPdComponent;
  let fixture: ComponentFixture<AllTasksPdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllTasksPdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllTasksPdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
