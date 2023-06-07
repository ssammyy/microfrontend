import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproveDraftStdComponent } from './approve-draft-std.component';

describe('ApproveDraftStdComponent', () => {
  let component: ApproveDraftStdComponent;
  let fixture: ComponentFixture<ApproveDraftStdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproveDraftStdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApproveDraftStdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
