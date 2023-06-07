import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComStandardLevyComponent } from './com-standard-levy.component';

describe('ComStandardLevyComponent', () => {
  let component: ComStandardLevyComponent;
  let fixture: ComponentFixture<ComStandardLevyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComStandardLevyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComStandardLevyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
