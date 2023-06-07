import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkExpiredComponent } from './dmark-expired.component';

describe('DmarkExpiredComponent', () => {
  let component: DmarkExpiredComponent;
  let fixture: ComponentFixture<DmarkExpiredComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkExpiredComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkExpiredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
