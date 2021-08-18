import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PVOCComponent } from './pvoc.component';

describe('PVOCComponent', () => {
  let component: PVOCComponent;
  let fixture: ComponentFixture<PVOCComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PVOCComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PVOCComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
