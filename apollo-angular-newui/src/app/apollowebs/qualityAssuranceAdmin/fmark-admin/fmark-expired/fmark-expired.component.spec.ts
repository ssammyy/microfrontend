import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkExpiredComponent } from './fmark-expired.component';

describe('FmarkExpiredComponent', () => {
  let component: FmarkExpiredComponent;
  let fixture: ComponentFixture<FmarkExpiredComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkExpiredComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkExpiredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
