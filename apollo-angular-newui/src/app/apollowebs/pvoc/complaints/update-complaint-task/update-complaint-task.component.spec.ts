import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateComplaintTaskComponent } from './update-complaint-task.component';

describe('UpdateComplaintTaskComponent', () => {
  let component: UpdateComplaintTaskComponent;
  let fixture: ComponentFixture<UpdateComplaintTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateComplaintTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateComplaintTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
