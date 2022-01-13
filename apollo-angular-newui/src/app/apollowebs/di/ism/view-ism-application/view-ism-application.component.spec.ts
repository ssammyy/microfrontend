import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewIsmApplicationComponent } from './view-ism-application.component';

describe('ViewIsmApplicationComponent', () => {
  let component: ViewIsmApplicationComponent;
  let fixture: ComponentFixture<ViewIsmApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewIsmApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewIsmApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
