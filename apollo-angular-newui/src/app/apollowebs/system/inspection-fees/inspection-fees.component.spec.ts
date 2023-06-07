import {ComponentFixture, TestBed} from '@angular/core/testing';

import {InspectionFeesComponent} from './inspection-fees.component';

describe('InspectionFeesComponent', () => {
  let component: InspectionFeesComponent;
  let fixture: ComponentFixture<InspectionFeesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InspectionFeesComponent]
    })
        .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InspectionFeesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
