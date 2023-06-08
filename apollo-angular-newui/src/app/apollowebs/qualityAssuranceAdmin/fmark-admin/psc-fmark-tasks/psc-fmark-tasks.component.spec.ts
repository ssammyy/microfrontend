import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PscFmarkTasksComponent } from './psc-fmark-tasks.component';

describe('PscFmarkTasksComponent', () => {
  let component: PscFmarkTasksComponent;
  let fixture: ComponentFixture<PscFmarkTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PscFmarkTasksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PscFmarkTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
