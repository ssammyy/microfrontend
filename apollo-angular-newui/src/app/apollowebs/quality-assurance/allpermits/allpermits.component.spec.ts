import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllpermitsComponent } from './allpermits.component';

describe('AllpermitsComponent', () => {
  let component: AllpermitsComponent;
  let fixture: ComponentFixture<AllpermitsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllpermitsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllpermitsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
