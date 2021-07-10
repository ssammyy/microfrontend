import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FmarkComponent } from './fmark.component';

describe('FmarkComponent', () => {
  let component: FmarkComponent;
  let fixture: ComponentFixture<FmarkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FmarkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FmarkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
