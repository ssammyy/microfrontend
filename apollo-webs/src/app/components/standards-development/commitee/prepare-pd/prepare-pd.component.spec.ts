import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PreparePdComponent } from './prepare-pd.component';

describe('PreparePdComponent', () => {
  let component: PreparePdComponent;
  let fixture: ComponentFixture<PreparePdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PreparePdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PreparePdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
