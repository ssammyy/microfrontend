import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PdDetailsComponent } from './pd-details.component';

describe('PdDetailsComponent', () => {
  let component: PdDetailsComponent;
  let fixture: ComponentFixture<PdDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PdDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PdDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
