import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DmarkComponent } from './dmark.component';

describe('DmarkComponent', () => {
  let component: DmarkComponent;
  let fixture: ComponentFixture<DmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
