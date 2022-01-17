import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndustrialSparesComponent } from './industrial-spares.component';

describe('IndustrialSparesComponent', () => {
  let component: IndustrialSparesComponent;
  let fixture: ComponentFixture<IndustrialSparesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IndustrialSparesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IndustrialSparesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
