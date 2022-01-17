import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsignmentDocumentListComponent } from './consignment-document-list.component';

describe('ConsignmentDocumentListComponent', () => {
  let component: ConsignmentDocumentListComponent;
  let fixture: ComponentFixture<ConsignmentDocumentListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsignmentDocumentListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsignmentDocumentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
