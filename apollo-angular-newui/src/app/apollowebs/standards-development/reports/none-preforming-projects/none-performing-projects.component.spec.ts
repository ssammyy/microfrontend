import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NonePerformingProjectsComponent } from './none-performing-projects.component';

describe('NonePerformingProjectsComponent', () => {
  let component: NonePerformingProjectsComponent;
  let fixture: ComponentFixture<NonePerformingProjectsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NonePerformingProjectsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NonePerformingProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
