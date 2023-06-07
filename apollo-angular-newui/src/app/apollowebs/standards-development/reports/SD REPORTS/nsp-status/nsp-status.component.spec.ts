import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NspStatusComponent } from './nsp-status.component';

describe('NspStatusComponent', () => {
  let component: NspStatusComponent;
  let fixture: ComponentFixture<NspStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NspStatusComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NspStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
