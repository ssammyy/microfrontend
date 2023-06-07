import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaDetailsComponent } from './sta-details.component';

describe('StaDetailsComponent', () => {
  let component: StaDetailsComponent;
  let fixture: ComponentFixture<StaDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StaDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StaDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
