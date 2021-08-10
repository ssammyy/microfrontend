import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaHopTasksComponent } from './nwa-hop-tasks.component';

describe('NwaHopTasksComponent', () => {
  let component: NwaHopTasksComponent;
  let fixture: ComponentFixture<NwaHopTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaHopTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaHopTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
