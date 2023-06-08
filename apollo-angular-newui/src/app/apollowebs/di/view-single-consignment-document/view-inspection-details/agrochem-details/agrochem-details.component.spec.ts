import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgrochemDetailsComponent } from './agrochem-details.component';

describe('AgrochemDetailsComponent', () => {
  let component: AgrochemDetailsComponent;
  let fixture: ComponentFixture<AgrochemDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgrochemDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgrochemDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
