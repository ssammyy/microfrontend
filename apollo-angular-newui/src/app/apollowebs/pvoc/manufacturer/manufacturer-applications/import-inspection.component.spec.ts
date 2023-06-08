import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ImportInspectionComponent} from './import-inspection.component';

describe('ImportInspectionComponent', () => {
  let component: ImportInspectionComponent;
  let fixture: ComponentFixture<ImportInspectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ImportInspectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportInspectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
