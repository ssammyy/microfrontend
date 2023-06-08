import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateWaiverTaskComponent } from './update-waiver-task.component';

describe('UpdateWaiverTaskComponent', () => {
  let component: UpdateWaiverTaskComponent;
  let fixture: ComponentFixture<UpdateWaiverTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateWaiverTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateWaiverTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
