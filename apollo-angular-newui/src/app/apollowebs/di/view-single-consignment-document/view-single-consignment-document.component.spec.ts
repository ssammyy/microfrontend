import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSingleConsignmentDocumentComponent } from './view-single-consignment-document.component';

describe('ViewSingleConsignmentDocumentComponent', () => {
  let component: ViewSingleConsignmentDocumentComponent;
  let fixture: ComponentFixture<ViewSingleConsignmentDocumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ViewSingleConsignmentDocumentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewSingleConsignmentDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
