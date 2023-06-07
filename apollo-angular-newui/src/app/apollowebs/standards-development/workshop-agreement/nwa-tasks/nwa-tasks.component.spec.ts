import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaTasksComponent } from './nwa-tasks.component';

describe('NwaTasksComponent', () => {
  let component: NwaTasksComponent;
  let fixture: ComponentFixture<NwaTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
