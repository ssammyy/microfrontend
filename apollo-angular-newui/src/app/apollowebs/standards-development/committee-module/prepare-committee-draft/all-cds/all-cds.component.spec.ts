import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllCdsComponent } from './all-cds.component';

describe('AllCdsComponent', () => {
  let component: AllCdsComponent;
  let fixture: ComponentFixture<AllCdsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AllCdsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AllCdsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
