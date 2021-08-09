import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkApplicationComponent } from './fmark-application.component';

describe('FmarkApplicationComponent', () => {
  let component: FmarkApplicationComponent;
  let fixture: ComponentFixture<FmarkApplicationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkApplicationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkApplicationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
