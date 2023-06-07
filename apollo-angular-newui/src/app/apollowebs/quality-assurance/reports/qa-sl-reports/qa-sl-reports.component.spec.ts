import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QaSlReportsComponent } from './qa-sl-reports.component';

describe('QaSlReportsComponent', () => {
  let component: QaSlReportsComponent;
  let fixture: ComponentFixture<QaSlReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QaSlReportsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QaSlReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
