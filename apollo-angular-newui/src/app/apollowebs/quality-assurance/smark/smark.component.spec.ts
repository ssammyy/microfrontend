import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmarkComponent } from './smark.component';

describe('SmarkComponent', () => {
  let component: SmarkComponent;
  let fixture: ComponentFixture<SmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
