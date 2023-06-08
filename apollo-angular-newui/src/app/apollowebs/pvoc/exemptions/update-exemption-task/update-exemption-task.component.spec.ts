import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateExemptionTaskComponent } from './update-exemption-task.component';

describe('UpdateExemptionTaskComponent', () => {
  let component: UpdateExemptionTaskComponent;
  let fixture: ComponentFixture<UpdateExemptionTaskComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateExemptionTaskComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateExemptionTaskComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
