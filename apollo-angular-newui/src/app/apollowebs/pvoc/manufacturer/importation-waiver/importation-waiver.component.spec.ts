import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ImportationWaiverComponent} from './importation-waiver.component';

describe('ImportationWaiverComponent', () => {
  let component: ImportationWaiverComponent;
  let fixture: ComponentFixture<ImportationWaiverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ImportationWaiverComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportationWaiverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
