import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NwaKnwSecTasksComponent } from './nwa-knw-sec-tasks.component';

describe('NwaKnwSecTasksComponent', () => {
  let component: NwaKnwSecTasksComponent;
  let fixture: ComponentFixture<NwaKnwSecTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NwaKnwSecTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NwaKnwSecTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
