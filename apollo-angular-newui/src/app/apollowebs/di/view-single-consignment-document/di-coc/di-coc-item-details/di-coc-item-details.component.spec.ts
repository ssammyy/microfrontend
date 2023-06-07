import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiCocItemDetailsComponent } from './di-coc-item-details.component';

describe('DiCocItemDetailsComponent', () => {
  let component: DiCocItemDetailsComponent;
  let fixture: ComponentFixture<DiCocItemDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DiCocItemDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DiCocItemDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
