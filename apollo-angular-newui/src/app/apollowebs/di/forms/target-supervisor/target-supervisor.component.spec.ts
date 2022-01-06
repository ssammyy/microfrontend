import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TargetSupervisorComponent } from './target-supervisor.component';

describe('TargetSupervisorComponent', () => {
  let component: TargetSupervisorComponent;
  let fixture: ComponentFixture<TargetSupervisorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TargetSupervisorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TargetSupervisorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
