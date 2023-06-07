import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkExpiredComponent } from './smark-expired.component';

describe('SmarkExpiredComponent', () => {
  let component: SmarkExpiredComponent;
  let fixture: ComponentFixture<SmarkExpiredComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkExpiredComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkExpiredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
