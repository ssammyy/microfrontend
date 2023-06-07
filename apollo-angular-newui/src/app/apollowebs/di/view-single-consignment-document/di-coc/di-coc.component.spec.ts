import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiCocComponent } from './di-coc.component';

describe('DiCocComponent', () => {
  let component: DiCocComponent;
  let fixture: ComponentFixture<DiCocComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DiCocComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DiCocComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
