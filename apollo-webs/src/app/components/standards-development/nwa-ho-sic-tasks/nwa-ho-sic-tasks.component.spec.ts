import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaHoSicTasksComponent } from './nwa-ho-sic-tasks.component';

describe('NwaHoSicTasksComponent', () => {
  let component: NwaHoSicTasksComponent;
  let fixture: ComponentFixture<NwaHoSicTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaHoSicTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaHoSicTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
