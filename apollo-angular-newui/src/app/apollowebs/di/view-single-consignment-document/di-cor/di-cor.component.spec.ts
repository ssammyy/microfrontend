import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiCorComponent } from './di-cor.component';

describe('DiCorComponent', () => {
  let component: DiCorComponent;
  let fixture: ComponentFixture<DiCorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DiCorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DiCorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
